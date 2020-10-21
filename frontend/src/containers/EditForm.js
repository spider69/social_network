import React, { useState, useEffect } from "react";
import { FormGroup, FormControl, FormLabel } from "react-bootstrap";
import { useParams } from "react-router-dom";
import { useHistory } from "react-router-dom";
import LoaderButton from "../components/LoaderButton";
import { useAppContext } from "../libs/contextLib";
import { handleErrors, onError } from "../libs/errorLib";
import "./UserForms.css"

function useFormFields(initialState) {
    const [fields, setValues] = useState(initialState);
  
    return [
      fields,
      function(newFields) {
        setValues(newFields);
      },
      function(event) {
        if (event.target.id === "age") {
          setValues({...fields, [event.target.id]: +event.target.value});
        } else {
          setValues({...fields, [event.target.id]: event.target.value});
        }
      }
    ];
  }

export default function UserForms() {
    const { id } = useParams();
    const { userId } = useAppContext();
    const history = useHistory();
    const [isLoading, setIsLoading] = useState(false);
    const [form, setForm, handleFormChange] = useFormFields(null);

    useEffect(() => {
        async function onLoad() {
            try {
                let requestedForm = await fetch(`${window.location.origin}/get_form/${id}`)
                    .then(handleErrors)
                    .then(response => response.json())
                setForm(requestedForm);
            } catch (e) {
                onError(e);
            }
        }

        onLoad();
    }, [id]) // eslint-disable-line react-hooks/exhaustive-deps

    async function handleSubmit(event) {
        event.preventDefault();

        setIsLoading(true);

        try {
            await fetch(`${window.location.origin}/update_form/${id}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                  },
                body: JSON.stringify(form)
            }).then(handleErrors)
            setIsLoading(false);
            history.push(`/user_forms/${id}`)
        } catch (e) {
            onError(e)
            setIsLoading(false);
        }
    }

    return (
        <div className="Edit form">
            {form && id === userId && (<form onSubmit={handleSubmit}>
                <FormGroup controlId="firstName">
                    <FormLabel>First name</FormLabel>
                    <FormControl autoFocus type="text" value={form.firstName} onChange={handleFormChange}/>
                </FormGroup>
                <FormGroup controlId="lastName">
                    <FormLabel>Last name</FormLabel>
                    <FormControl type="text" value={form.lastName} onChange={handleFormChange}/>
                </FormGroup>
                <FormGroup controlId="age">
                    <FormLabel>Age</FormLabel>
                    <FormControl type="number" value={form.age} onChange={handleFormChange}/>
                </FormGroup>
                <FormGroup controlId="gender">
                    <FormLabel>Gender</FormLabel>
                    <FormControl as="select" value={form.gender} onChange={handleFormChange}>
                        <option></option>
                        <option>M</option>
                        <option>F</option>
                    </FormControl>
                </FormGroup>
                <FormGroup controlId="interests">
                    <FormLabel>Interests</FormLabel>
                    <FormControl type="text" value={form.interests} onChange={handleFormChange}/>
                </FormGroup>
                <FormGroup controlId="city">
                    <FormLabel>City</FormLabel>
                    <FormControl type="text" value={form.city} onChange={handleFormChange}/>
                </FormGroup>
                <LoaderButton
                    block
                    type="submit"
                    isLoading={isLoading}
                >
                    Submit
               </LoaderButton>
            </form>
            )}
        </div>
    );
}