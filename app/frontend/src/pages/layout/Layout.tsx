import { Outlet, NavLink, Link } from "react-router-dom";

import github from "../../assets/github.svg";

import styles from "./Layout.module.css";

import { useLogin } from "../../authConfig";

import { LoginButton } from "../../components/LoginButton";

const Layout = () => {
    return (
        <div className={styles.layout}>
            <header className={styles.header} role={"banner"}>
                <div className={styles.headerContainer}>
                    <Link to="/" className={styles.headerTitleContainer}>
                        <h3 className={styles.headerTitle}>Controles Empresariales</h3>
                    </Link>
                    <nav>
                        <ul className={styles.headerNavList}>
                            <li>
                                <NavLink to="/" className={({ isActive }) => (isActive ? styles.headerNavPageLinkActive : styles.headerNavPageLink)}>
                                    Chat bancario
                                </NavLink>
                            </li>

                            <li className={styles.headerNavLeftMargin}>
                                <a href="https://controlesempresariales.com" target={"_blank"} title="Sitio de Controles Empresariales" rel="noreferrer">
                                    controlesempresariales.com
                                </a>
                            </li>

                            <li className={styles.headerNavLeftMargin}>
                                <a href="https://github.com/dantelmomsft/personal-finance-assistant-java" target={"_blank"} title="Repositorio en GitHub" rel="noreferrer">
                                    <img
                                        src={github}
                                        alt="Logotipo de GitHub"
                                        aria-label="Enlace al repositorio de GitHub"
                                        width="20px"
                                        height="20px"
                                        className={styles.githubLogo}
                                    />
                                </a>
                            </li>
                        </ul>
                    </nav>
                    <h4 className={styles.headerRightText}>ExperiencIA BancarIA COEM</h4>
                    {useLogin && <LoginButton />}
                </div>
            </header>

            <Outlet />
        </div>
    );
};

export default Layout;
