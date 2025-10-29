import { useCallback, useEffect, useMemo, useRef, useState } from "react";
import { DefaultButton, Toggle } from "@fluentui/react";

import { BACKEND_URI } from "../../api";

import styles from "./LogStreamPanel.module.css";

type LogEvent = {
    timestamp: string;
    level: string;
    logger: string;
    thread: string;
    message: string;
    exception?: string;
    mdc?: Record<string, string>;
};

const formatLogLine = (entry: LogEvent) => {
    const context = entry.mdc && Object.keys(entry.mdc).length > 0 ? ` ${JSON.stringify(entry.mdc)}` : "";
    const exception = entry.exception ? `\n${entry.exception}` : "";
    return `${entry.timestamp} [${entry.thread}] ${entry.level} ${entry.logger} - ${entry.message}${context}${exception}`;
};

const normalizedBackendUri = (BACKEND_URI || "").replace(/\/$/, "");
const API_BASE = normalizedBackendUri.length > 0 ? normalizedBackendUri : "/api";
const STREAM_ENDPOINT = `${API_BASE}/logs/stream`;
const HISTORY_ENDPOINT = `${API_BASE}/logs`;

type LogStreamPanelProps = {
    currentUserEmail?: string;
};

export const LogStreamPanel = ({ currentUserEmail }: LogStreamPanelProps) => {
    const [isVisible, setIsVisible] = useState<boolean>(true);
const STREAM_ENDPOINT = "/api/logs/stream";
const HISTORY_ENDPOINT = "/api/logs";

export const LogStreamPanel = () => {
    const [isVisible, setIsVisible] = useState<boolean>(false);
    const [logs, setLogs] = useState<LogEvent[]>([]);
    const [error, setError] = useState<string | undefined>();
    const eventSourceRef = useRef<EventSource | null>(null);
    const logEndRef = useRef<HTMLDivElement | null>(null);
    const mcpLogEndRef = useRef<HTMLDivElement | null>(null);

    const handleToggle = (_: React.MouseEvent<HTMLElement>, checked?: boolean) => {
        setIsVisible(!!checked);
    };

    const appendLog = useCallback((entry: LogEvent) => {
        setLogs(prev => {
            const next = [...prev, entry];
            if (next.length > 500) {
                return next.slice(next.length - 500);
            }
            return next;
        });
    }, []);

    const stopStreaming = useCallback(() => {
        eventSourceRef.current?.close();
        eventSourceRef.current = null;
    }, []);

    const startStreaming = useCallback(() => {
        stopStreaming();
        setError(undefined);

        const controller = new AbortController();

        fetch(HISTORY_ENDPOINT, { signal: controller.signal })
            .then(async response => {
                if (!response.ok) {
                    throw new Error("No se pudo obtener el historial de logs");
                }
                const history = (await response.json()) as LogEvent[];
                setLogs(history);
                setError(undefined);
            })
            .catch(err => {
                if (err.name !== "AbortError") {
                    setError(err.message);
                }
            });

        const eventSource = new EventSource(STREAM_ENDPOINT, { withCredentials: true });
        const eventSource = new EventSource(STREAM_ENDPOINT);
        eventSource.addEventListener("log", event => {
            try {
                const parsed = JSON.parse((event as MessageEvent).data) as LogEvent;
                setError(undefined);
                appendLog(parsed);
            } catch (e) {
                console.error("Error al parsear evento de log", e);
            }
        });
        eventSource.onerror = () => {
            setError("Se perdió la conexión con el servicio de logs");
        };

        eventSourceRef.current = eventSource;

        return () => {
            controller.abort();
            stopStreaming();
        };
    }, [appendLog, stopStreaming]);

    useEffect(() => {
        if (isVisible) {
            const cleanup = startStreaming();
            return () => {
                cleanup?.();
            };
        }

        setLogs([]);
        setError(undefined);
        stopStreaming();
    }, [isVisible, startStreaming, stopStreaming]);

    useEffect(() => {
        if (isVisible) {
            logEndRef.current?.scrollIntoView({ behavior: "smooth" });
        }
    }, [logs, isVisible]);

    const mcpLogs = useMemo(
        () => logs.filter(entry => entry.logger?.toLowerCase().includes("mcp") || /Executing .*|Response from/.test(entry.message)),
        [logs]
    );

    useEffect(() => {
        if (isVisible && mcpLogs.length > 0) {
            mcpLogEndRef.current?.scrollIntoView({ behavior: "smooth" });
        }
    }, [mcpLogs, isVisible]);

    useEffect(() => () => stopStreaming(), [stopStreaming]);

    const downloadContent = useMemo(() => logs.map(formatLogLine).join("\n\n"), [logs]);

    const onDownload = () => {
        const blob = new Blob([downloadContent], { type: "text/plain;charset=utf-8" });
        const url = URL.createObjectURL(blob);
        const link = document.createElement("a");
        link.href = url;
        const timestamp = new Date().toISOString().replace(/[:.]/g, "-");
        link.download = `assistant-logs-${timestamp}.txt`;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        URL.revokeObjectURL(url);
    };

    const hasLogs = logs.length > 0;
    const hasMcpLogs = mcpLogs.length > 0;

    return (
        <aside className={styles.wrapper} aria-live="polite">
            <div className={styles.header}>
                <h2 className={styles.title}>Monitoreo de logs</h2>
                <Toggle
                    onText="Visible"
                    offText="Oculto"
                    checked={isVisible}
                    onChange={handleToggle}
                    aria-label="Activar o desactivar el panel de logs"
                />
            </div>
            <p className={styles.description}>
                {currentUserEmail
                    ? `Observa la actividad generada para ${currentUserEmail}.`
                    : "Selecciona un usuario para contextualizar los eventos registrados."}
                Activa el panel para seguir en tiempo real los eventos internos de la aplicación.
            </p>
            <DefaultButton
                iconProps={{ iconName: "Download" }}
                text="Descargar"
                className={styles.downloadButton}
                onClick={onDownload}
                disabled={!isVisible || !hasLogs}
            />
            <div className={isVisible ? styles.sections : styles.hidden}>
                <section className={styles.section} aria-label="Bitácora general">
                    <div className={styles.sectionHeader}>
                        <h3 className={styles.sectionTitle}>Bitácora general</h3>
                    </div>
                    <div className={styles.logViewport}>
                        {error ? (
                            <div className={styles.feedback} role="alert">
                                {error}
                            </div>
                        ) : !hasLogs ? (
                            <div className={styles.feedback}>Aún no se han producido eventos.</div>
                        ) : (
                            logs.map((entry, index) => (
                                <pre key={`${entry.timestamp}-${index}`} className={styles.logEntry}>
                                    {formatLogLine(entry)}
                                </pre>
                            ))
                        )}
                        <div ref={logEndRef} />
                    </div>
                </section>
                <section className={styles.section} aria-label="Interacciones MCP">
                    <div className={styles.sectionHeader}>
                        <h3 className={styles.sectionTitle}>Intercambios con servidores MCP</h3>
                        <span className={styles.mcpBadge}>{hasMcpLogs ? `${mcpLogs.length}` : "0"}</span>
                    </div>
                    <div className={styles.logViewport}>
                        {!hasMcpLogs ? (
                            <div className={styles.feedback}>No hay solicitudes MCP registradas todavía.</div>
                        ) : (
                            mcpLogs.map((entry, index) => (
                                <pre key={`mcp-${entry.timestamp}-${index}`} className={styles.logEntry}>
                                    {formatLogLine(entry)}
                                </pre>
                            ))
                        )}
                        <div ref={mcpLogEndRef} />
                    </div>
                </section>
                disabled={!isVisible || logs.length === 0}
            />
            <div className={isVisible ? styles.logViewport : styles.logViewportHidden}>
                {error ? (
                    <div className={styles.feedback} role="alert">
                        {error}
                    </div>
                ) : logs.length === 0 ? (
                    <div className={styles.feedback}>No hay eventos para mostrar todavía.</div>
                ) : (
                    logs.map((entry, index) => (
                        <pre key={`${entry.timestamp}-${index}`} className={styles.logEntry}>
                            {formatLogLine(entry)}
                        </pre>
                    ))
                )}
                <div ref={logEndRef} />
            </div>
        </aside>
    );
};

export default LogStreamPanel;
