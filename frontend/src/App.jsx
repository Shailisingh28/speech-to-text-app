import { useState } from "react";

import Register from "./pages/Register";
import Login from "./pages/Login";
import Upload from "./pages/Upload";

function App() {

  const [isLoggedIn, setIsLoggedIn] =
    useState(
      localStorage.getItem("token")
    );

  const handleLogin = () => {

    setIsLoggedIn(true);
  };

  const handleLogout = () => {

    localStorage.removeItem("token");

    setIsLoggedIn(false);
  };

  return (

    <div
  className="
    container
    py-5
    min-vh-100
  "
>

      <div className="text-center mb-5">

        <h1 className="fw-bold text-primary">

          Speech To Text App

        </h1>

        <p className="text-muted">

          AI Powered Audio Transcription

        </p>

      </div>

      {
        !isLoggedIn ? (

          <div className="row justify-content-center">

            <div className="col-md-5 mb-4">

              <Register />

            </div>

            <div className="col-md-5 mb-4">

              <Login onLogin={handleLogin} />

            </div>

          </div>

        ) : (

          <div>

            <div
              className="
                d-flex
                justify-content-between
                align-items-center
                mb-4
              "
            >

              <h3>

                Welcome User 👋

              </h3>

              <button
                className="btn btn-dark"

                onClick={handleLogout}
              >

                Logout

              </button>

            </div>

            <Upload />

          </div>

        )
      }

    </div>
  );
}

export default App;