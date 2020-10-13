import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { useHistory } from "react-router-dom";
import { Button } from "react-bootstrap";
import { useAppContext } from "../libs/contextLib";
import { handleErrors, onError } from "../libs/errorLib";
import "./UserForms.css"

export default function UserForms() {
    const { id } = useParams();
    const { userId } = useAppContext();
    const history = useHistory();
    const [form, setForm] = useState(null);

    useEffect(() => {
        async function onLoad() {
            try {
                let requestedForm = await fetch(`http://localhost:8080/get_form/${id}`)
                    .then(handleErrors)
                    .then(response => response.json())
                setForm(requestedForm);
            } catch (e) {
                onError(e);
            }
        }

        onLoad();
    }, [id]);

    function onEdit() {
        history.push(`/edit_form/${userId}`)
    }

    return (
        <div className="UserForms">
            {form && (
                <div className="user-deets">
                    <h3>
                        {form.firstName} {form.lastName}
                    </h3>
                    <h2>
                        <strong>Age</strong> {form.age}
                    </h2>
                    <h2>
                        <strong>Gender</strong> {form.gender}
                    </h2>
                    <h2>
                        <strong>Interests</strong> {form.interests}
                    </h2>
                    <h2>
                        <strong>City</strong> {form.city}
                    </h2>
                    {id === userId && <Button block onClick={() => onEdit()} className="px-4">Edit form</Button>}
                </div>
            )}
        </div>
    );
}