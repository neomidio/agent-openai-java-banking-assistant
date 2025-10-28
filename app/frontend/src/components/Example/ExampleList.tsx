import { Example } from "./Example";

import styles from "./Example.module.css";

export type ExampleModel = {
    text: string;
    value: string;
};

const EXAMPLES: ExampleModel[] = [
    { text: "Quiero pagar mi recibo de luz", value: "Quiero pagar mi recibo de luz" },
    { text: "¿Cuáles han sido mis pagos este año?", value: "¿Cuáles han sido mis pagos este año?" },
    { text: "¿Cuál es el límite disponible de mi tarjeta?", value: "¿Cuál es el límite disponible de mi tarjeta?" }
];

interface Props {
    onExampleClicked: (value: string) => void;
}

export const ExampleList = ({ onExampleClicked }: Props) => {
    return (
        <ul className={styles.examplesNavList}>
            {EXAMPLES.map((x, i) => (
                <li key={i}>
                    <Example text={x.text} value={x.value} onClick={onExampleClicked} />
                </li>
            ))}
        </ul>
    );
};
