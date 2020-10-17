import React, { useState } from "react";
import { FormGroup, FormControl, FormLabel } from "react-bootstrap";
import LoaderButton from "../components/LoaderButton";
import { useHistory } from "react-router-dom";
import { useFormFields } from "../libs/hooksLib";
import { onError } from "../libs/errorLib";
import "./Signup.css";

export default function Signup() {
    const history = useHistory();
    const [fields, handleFieldChange] = useFormFields({
        email: "",
        userName: "",
        password: "",
        confirmPassword: ""
    });

    const [isLoading, setIsLoading] = useState(false);

    function validateForm() {
        return (
            fields.email.length > 0 &&
            fields.userName.length > 0 &&
            fields.password.length > 0 &&
            fields.password === fields.confirmPassword
        );
    }

    async function handleSignUpResponse(response) {
        let errorMessage = await response.text()
        if (!response.ok) {
          if (errorMessage === "User already exists") {
            throw errorMessage;
          } else {
            throw Error(response.statusText);
          }
        }
        return response;
      }

    async function handleSubmit(event) {
        event.preventDefault();

        setIsLoading(true);

        try {
            let formData = new FormData()
            formData.append('email', fields.email)
            formData.append('userName', fields.userName)
            formData.append('password', fields.password)
            await fetch('https://localhost:8080/create_user', {
                method: 'POST',
                body: formData
            }).then(handleSignUpResponse)
            setIsLoading(false);
            history.push("/login");
        } catch (e) {
            onError(e)
            setIsLoading(false);
        }
    }

    return (
        <div className="Signup">
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
                <FormGroup controlId="userName">
                    <FormLabel>User name</FormLabel>
                    <FormControl
                        type="text"
                        value={fields.userName}
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
                <FormGroup controlId="confirmPassword">
                    <FormLabel>Confirm Password</FormLabel>
                    <FormControl
                        type="password"
                        onChange={handleFieldChange}
                        value={fields.confirmPassword}
                    />
                </FormGroup>
                <LoaderButton
                    block
                    type="submit"
                    isLoading={isLoading}
                    disabled={!validateForm()}
                >
                    Sign up
               </LoaderButton>
            </form>
        </div>
    );
}