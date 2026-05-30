import { useState } from "react";
import axios from "axios";

function Register() {

    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const handleSubmit = async (e) => {

        e.preventDefault();

        try {

            const response = await axios.post(
                `${import.meta.env.VITE_API_URL}/auth/register`,
                {
                    name,
                    email,
                    password
                }
            );

            alert(response.data);

setName("");
setEmail("");
setPassword("");

        } catch (error) {

            alert("Registration Failed");
        }
    };

    return (
        <div className="card shadow p-4 border-0 rounded-4">

            <h2 className="text-center mb-4 text-primary">
                Register
            </h2>

            <form onSubmit={handleSubmit}>

                <div className="mb-3">
                    <input
                        type="text"
                        className="form-control"
                        placeholder="Enter Name"
                        value={name}
                        onChange={(e) =>
                            setName(e.target.value)
                        }
                    />
                </div>

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
                    className="btn btn-primary w-100"
                    type="submit"
                >
                    Register
                </button>

            </form>

        </div>
    );
}

export default Register;
