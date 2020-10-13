import React from "react";
import { useHistory } from "react-router-dom";
import { useAppContext } from "../libs/contextLib";
import "./Home.css";

export default function Home() {
  const history = useHistory();
  const { userId } = useAppContext();

  function renderLander() {
    return (
      <div className="lander">
        <h1>Honey cat's social network</h1>
        <p>Meow meow meow</p>
      </div>
    );
  }

  function redirectToForm() {
    history.push(`/user_forms/${userId}`);
  }

  return (
    <div className="Home">
      {userId ? redirectToForm() : renderLander()}
    </div>
  );
}