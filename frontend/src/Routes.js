import React from "react";
import { Route, Switch } from "react-router-dom";
import Home from "./containers/Home";
import NotFound from "./containers/NotFound";
import Login from "./containers/Login";
import Signup from "./containers/Signup";
import AuthenticatedRoute from "./components/AuthenticatedRoute";
import UnauthenticatedRoute from "./components/UnauthenticatedRoute";
import UserForms from "./containers/UserForms";
import EditForm from "./containers/EditForm";
import Users from "./containers/Users"

export default function Routes() {
    return (
        <Switch>
            <Route exact path="/">
                <Home />
            </Route>
            <AuthenticatedRoute exact path="/users">
                <Users />
            </AuthenticatedRoute>
            <AuthenticatedRoute exact path="/user_forms/:id">
                <UserForms />
            </AuthenticatedRoute>
            <AuthenticatedRoute exact path="/edit_form/:id">
                <EditForm />
            </AuthenticatedRoute>
            <AuthenticatedRoute exact path="/home">
                <Home />
            </AuthenticatedRoute>
            <UnauthenticatedRoute exact path="/login">
                <Login />
            </UnauthenticatedRoute>
            <UnauthenticatedRoute exact path="/signup">
                <Signup />
            </UnauthenticatedRoute>
            <Route>
                <NotFound />
            </Route>

        </Switch>
    );
}