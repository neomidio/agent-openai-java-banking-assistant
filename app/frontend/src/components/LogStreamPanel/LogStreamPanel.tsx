import { useCallback, useEffect, useMemo, useRef, useState } from "react";
import { DefaultButton, Toggle } from "@fluentui/react";

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

const STREAM_ENDPOINT = "/api/logs/stream";
const HISTORY_ENDPOINT = "/api/logs";

export const LogStreamPanel = () => {
    const [isVisible, setIsVisible] = useState<boolean>(false);
    const [logs, setLogs] = useState<LogEvent[]>([]);
    const [error, setError] = useState<string | undefined>();
    const eventSourceRef = useRef<EventSource | null>(null);
    const logEndRef = useRef<HTMLDivElement | null>(null);

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
                Activa el panel para seguir en tiempo real los eventos internos de la aplicación.
            </p>
            <DefaultButton
                iconProps={{ iconName: "Download" }}
                text="Descargar"
                className={styles.downloadButton}
                onClick={onDownload}
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
