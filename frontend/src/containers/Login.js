import React, { useState } from "react";
import { useAppContext } from "../libs/contextLib";
import { FormGroup, FormControl, FormLabel } from "react-bootstrap";
import LoaderButton from "../components/LoaderButton";
import { useHistory } from "react-router-dom";
import { useFormFields } from "../libs/hooksLib";
import { onError } from "../libs/errorLib";
import "./Login.css";


export default function Login() {
    const history = useHistory();
    const { setUserId } = useAppContext();
    const [isLoading, setIsLoading] = useState(false);
    const [fields, handleFieldChange] = useFormFields({
        email: "",
        password: ""
    });

    function validateForm() {
        return fields.email.length > 0 && fields.password.length > 0;
    }

    async function handleLoginResponse(response) {
        let responseText = await response.text()
        if (!response.ok) {
            if (responseText === "Wrong credentials") {
                throw responseText;
            } else {
                throw Error(response.statusText);
            }
        }
        return responseText;
    }

    async function handleSubmit(event) {
        event.preventDefault();

        setIsLoading(true);

        try {
            let formData = new FormData()
            formData.append('login', fields.email)
            formData.append('password', fields.password)
            let userId = await fetch(`${window.location.origin}/sign_in`, {
                method: 'POST',
                body: formData
            }).then(handleLoginResponse)
            setUserId(userId)
            history.push("/");
        } catch (e) {
            onError(e);
            setIsLoading(false);
        }
    }

    return (
        <div className="Login">
            <form onSubmit={handleSubmit}>
                <FormGroup controlId="email">
                    <FormLabel>Email</FormLabel>
                    <FormControl
                        autoFocus
                        type="email"
                        value={fields.email}
                        onChange={handleFieldChange}
                    />
                </FormGroup>
                <FormGroup controlId="password">
                    <FormLabel>Password</FormLabel>
                    <FormControl
                        type="password"
                        value={fields.password}
                        onChange={handleFieldChange}
                    />
                </FormGroup>
                <LoaderButton
                    block
                    type="submit"
                    isLoading={isLoading}
                    disabled={!validateForm()}
                >
                    Login
               </LoaderButton>
            </form>
        </div>
    );
}