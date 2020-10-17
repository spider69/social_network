import React, { useState, useEffect } from "react";
import { ListGroup, ListGroupItem, Button } from "react-bootstrap";
import { LinkContainer } from "react-router-bootstrap";
import { useParams } from "react-router-dom";
import { useHistory } from "react-router-dom";
import { useAppContext } from "../libs/contextLib";
import { handleErrors, onError } from "../libs/errorLib";
import "./UserForms.css"

export default function UserForms() {
    const { id } = useParams();
    const { userId } = useAppContext();
    const history = useHistory();
    const [form, setForm] = useState(null);
    const [friends, setFriends] = useState([])

    useEffect(() => {
        async function onLoad() {
            try {
                let requestedForm = await fetch(`https://localhost:8080/get_form/${id}`)
                    .then(handleErrors)
                    .then(response => response.json())
                setForm(requestedForm);

                let requestedFriends = await fetch(`https://localhost:8080/get_friends/${id}`)
                    .then(handleErrors)
                    .then(response => response.json())
                setFriends(requestedFriends)
            } catch (e) {
                onError(e);
            }
        }

        onLoad();
    }, [id]);

    function onEdit() {
        history.push(`/edit_form/${userId}`)
    }

    async function onAddFriend() {
        try {
            await fetch(`https://localhost:8080/add_friend/?user=${userId}&friend=${id}`)
                .then(handleErrors)
        } catch (e) {
            onError(e);
        }
        window.location.reload(false);
    }

    async function onRemoveFriend() {
        try {
            await fetch(`https://localhost:8080/remove_friend/?user=${userId}&friend=${id}`)
                .then(handleErrors)
        } catch (e) {
            onError(e);
        }
        window.location.reload(false);
    }

    function renderFriendsList(friends) {
        return [{}].concat(friends).map((friend, i) =>
            i !== 0 && (
                <LinkContainer key={friend.id} to={`/user_forms/${friend.id}`}>
                    <ListGroupItem action>
                        {friend.firstName} {friend.lastName}
                    </ListGroupItem>
                </LinkContainer>
            )
        );
    }

    return (
        <div className="form">
            {form && (
                <div>
                    <h3>
                        {form.firstName} {form.lastName}
                    </h3>
                    {id === userId ?
                        <Button onClick={() => onEdit()} className="px-4">Edit form</Button>
                        : friends.filter(f => f.id !== id).length === 0 ?
                            <Button variant="success" onClick={() => onAddFriend()} className="px-4">Add friend</Button>
                            :
                            <Button variant="danger" onClick={() => onRemoveFriend()} className="px-4">Remove friend</Button>
                    }
                    <div class="info">
                        <div class="left">
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
                        </div>
                        <div class="right">
                            <h2>
                                <strong>Friends</strong>
                                <ListGroup variant="flush">
                                    {renderFriendsList(friends)}
                                </ListGroup>
                            </h2>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}