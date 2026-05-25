import { useState } from "react";
import API from "../services/api";

function Upload() {

    const [file, setFile] = useState(null);

    const [transcript, setTranscript] =
        useState("");

    const [loading, setLoading] =
        useState(false);

    const handleUpload = async () => {

        if (!file) {

            alert("Please select audio file");

            return;
        }

        const formData = new FormData();

        formData.append("file", file);

        try {

            setLoading(true);

            const response = await API.post(
                "/speech/transcribe",
                formData,
                {
                    headers: {
                        "Content-Type":
                            "multipart/form-data",
                    },
                }
            );

            console.log(response.data);

            if (
                typeof response.data === "string"
            ) {

                setTranscript(response.data);

            } else if (
                response.data.transcript
            ) {

                setTranscript(
                    response.data.transcript
                );

            } else {

                setTranscript(
                    JSON.stringify(response.data)
                );
            }

        } catch (error) {

            console.log(error);

            alert(
                "Transcription Failed"
            );

        } finally {

            setLoading(false);
        }
    };

    return (

        <div className="card shadow p-4 border-0 rounded-4">

            <h2 className="text-center mb-4 text-danger">

                Upload Audio

            </h2>

            <input
                type="file"
                className="form-control mb-3"

                onChange={(e) =>
                    setFile(
                        e.target.files[0]
                    )
                }
            />

            <button
                className="btn btn-danger w-100 mb-4"

                onClick={handleUpload}
            >

                {
                    loading
                    ? "Processing..."
                    : "Transcribe Audio"
                }

            </button>

            <h4 className="mb-3">

                Transcript

            </h4>

            <div
                className="
                    border
                    rounded
                    p-3
                    bg-light
                "
            >

                {
                    transcript
                    ? transcript
                    : "Transcript will appear here..."
                }

            </div>

        </div>
    );
}

export default Upload;