import { useState } from "react";
import API from "../services/api";

function Login({ onLogin }) {

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const handleSubmit = async (e) => {

        e.preventDefault();

        try {

            const response = await API.post(
                "/auth/login",
                {
                    email,
                    password,
                }
            );

            localStorage.setItem(
                "token",
                response.data.token
            );

            alert("Login Successful");
            onLogin();

        } catch (error) {

            alert("Login Failed");
        }
    };

    return (
        <div className="card shadow p-4 border-0 rounded-4">

            <h2 className="text-center mb-4 text-success">
                Login
            </h2>

            <form onSubmit={handleSubmit}>

                <div className="mb-3">
                    <input
                        type="email"
                        className="form-control"
                        placeholder="Enter Email"
                        value={email}
                        onChange={(e) =>
                            setEmail(e.target.value)
                        }
                    />
                </div>

                <div className="mb-3">
                    <input
                        type="password"
                        className="form-control"
                        placeholder="Enter Password"
                        value={password}
                        onChange={(e) =>
                            setPassword(e.target.value)
                        }
                    />
                </div>

                <button
                    className="btn btn-success w-100"
                    type="submit"
                >
                    Login
                </button>

            </form>

        </div>
    );
}

export default Login;