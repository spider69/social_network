import React, { useState, useEffect } from "react";
import { AppContext } from "./libs/contextLib";
import { Nav, Navbar, Form, FormControl, Button } from "react-bootstrap";
import { useHistory } from "react-router-dom";
import { handleErrors, onError } from "./libs/errorLib";
import Routes from "./Routes";
import "./App.css"


function App() {
  const history = useHistory();
  const [isAuthenticating, setIsAuthenticating] = useState(true);
  const [isAuthenticated, userHasAuthenticated] = useState(false);

  useEffect(() => {
    onLoad();
  }, []);
  
  async function handleCurrentSessionResponse(response) {
    let errorMessage = await response.text()
    if (!response.ok) {
      if (errorMessage == "No current user") {
        throw errorMessage;
      } else {
        throw Error(response.statusText);
      }
    }
    return response;
  }

  async function onLoad() {
    try {
      await fetch('http://localhost:8080/current_session').then(handleCurrentSessionResponse);
      userHasAuthenticated(true);
    }
    catch (e) {
      if (e !== 'No current user') {
        onError(e);
      }
    }
  
    setIsAuthenticating(false);
  }

  async function handleLogout() {
    await fetch('http://localhost:8080/sign_out').then(handleErrors);
    userHasAuthenticated(false);
    history.push("/login");
  }

  return (
    !isAuthenticating &&
    <div className="App container">
      <Navbar expand="lg" bg="dark" variant="dark">
        <Navbar.Brand href="/"><h1>Honey cat socials</h1></Navbar.Brand>
        <Nav className="mr-auto">
          {isAuthenticated
            ? <Nav.Link onClick={handleLogout}><h3>Logout</h3></Nav.Link>
            : <>
              <Nav.Link href="login"><h3>Login</h3></Nav.Link>
              <Nav.Link href="signup"><h3>Sign up</h3></Nav.Link>
            </>
          }
        </Nav>
        <Form inline>
          <FormControl type="text" placeholder="Search" className="mr-sm-2" />
          <Button variant="outline-info">Search</Button>
        </Form>
      </Navbar>
      <AppContext.Provider value={{ isAuthenticated, userHasAuthenticated }}>
        <Routes />
      </AppContext.Provider>
    </div>
  );
}

export default App;