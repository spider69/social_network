import React, { useState, useEffect } from "react";
import { ListGroup, ListGroupItem } from "react-bootstrap";
import { LinkContainer } from "react-router-bootstrap";
import { useAppContext } from "../libs/contextLib";
import { handleErrors, onError } from "../libs/errorLib";
import "./Users.css";

export default function Users() {

  const [users, setUsers] = useState([]);
  const { isAuthenticated } = useAppContext();
  const [isLoading, setIsLoading] = useState(true);
  
  useEffect(() => {
    async function onLoad() {
      if (!isAuthenticated) {
        return;
      }
  
      try {
        let response = await fetch('http://localhost:8080/all_users')
          .then(handleErrors)
        let json = await response.json()
        setUsers(json);
      } catch (e) {
        onError(e);
      }
  
      setIsLoading(false);
    }
  
    onLoad();
  }, [isAuthenticated]);

  function renderUsersList(users) {
    return [{}].concat(users).map((user, i) =>
      i !== 0 ? (
        <LinkContainer key={user.email} to={`/user_forms/${user.email}`}>
          <ListGroupItem action>
            Name: {user.name}
          </ListGroupItem>
        </LinkContainer>
      ) : (
          <h1>All users</h1>
        )
    );
  }

  function renderLander() {
    return (
      <div className="lander">
        <h1>Honey cat's social network</h1>
        <p>Meow meow meow</p>
      </div>
    );
  }

  function renderUsers() {
    return (
      <div className="users">
        <ListGroup variant="flush">
          {!isLoading && renderUsersList(users)}
        </ListGroup>
      </div>
    );
  }

  return (
    <div className="Users">
      {isAuthenticated ? renderUsers() : renderLander()}
    </div>
  );
}