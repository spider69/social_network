import React, { useState, useEffect } from "react";
import { ListGroup, ListGroupItem, Form, FormControl } from "react-bootstrap";
import { LinkContainer } from "react-router-bootstrap";
import { useAppContext } from "../libs/contextLib";
import { handleErrors, onError } from "../libs/errorLib";
import "./Users.css";

export default function Users() {

  const [users, setUsers] = useState([]);
  const { userId } = useAppContext();
  const [isLoading, setIsLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState("");
  
  useEffect(() => {
    async function onLoad() {
      if (!userId) {
        return;
      }
  
      try {
        let allUsers = await fetch(`${window.location.origin}/all_users`)
          .then(handleErrors)
          .then(response => response.json())
        setUsers(allUsers)
      } catch (e) {
        onError(e);
      }
  
      setIsLoading(false);
    }
  
    onLoad();
  }, [userId]);

  function renderUsersList(users) {
    return [{}].concat(users).map((user, i) =>
      i !== 0 && (
        <LinkContainer key={user.id} to={`/user_forms/${user.id}`}>
          <ListGroupItem action>
             {user.firstName} {user.lastName}
          </ListGroupItem>
        </LinkContainer>
      ) 
    );
  }

  function handleChangeSearchQuery(e) {
    setSearchQuery(e.target.value)
  }

  async function onKeyPressHandler(e) {
    if (e.key === 'Enter') {
      e.preventDefault();
      try {
        let filteredUsers = await fetch(`${window.location.origin}/all_users/?filter=${searchQuery}`)
          .then(handleErrors)
          .then(response => response.json())
        setUsers(filteredUsers)
      } catch (e) {
        onError(e);
      }
    }
  }

  function renderUsers() {
    return (
      <div className="users">
        <Form inline>
          <FormControl type="text" placeholder="Search" className="mr-sm-2" onChange={handleChangeSearchQuery} onKeyPress={onKeyPressHandler} />
        </Form>
        <ListGroup variant="flush">
          {!isLoading && renderUsersList(users)}
        </ListGroup>
      </div>
    );
  }

  return (
    <div className="Users">
      {userId && renderUsers()}
    </div>
  );
}