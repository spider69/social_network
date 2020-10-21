(this["webpackJsonpauth-app-client"]=this["webpackJsonpauth-app-client"]||[]).push([[0],{43:function(e,t,a){},55:function(e,t,a){e.exports=a(76)},60:function(e,t,a){},62:function(e,t,a){},68:function(e,t,a){},69:function(e,t,a){},70:function(e,t,a){},72:function(e,t,a){},74:function(e,t,a){},75:function(e,t,a){},76:function(e,t,a){"use strict";a.r(t);var n=a(0),r=a.n(n),c=a(24),o=a.n(c),l=a(11),s=(a(60),a(5)),u=a.n(s),i=a(9),m=a(6),p=Object(n.createContext)(null);function d(){return Object(n.useContext)(p)}var E=a(80),f=a(82),h=a(7);function O(e){var t=e.toString();e instanceof Error||!e.message||(t=e.message),alert(t)}function v(e){if(!e.ok)throw Error(e.statusText);return e}a(62);function b(){var e=Object(h.k)(),t=d().userId;return r.a.createElement("div",{className:"Home"},t?void e.push("/user_forms/".concat(t)):r.a.createElement("div",{className:"lander"},r.a.createElement("h1",null,"Pavel Yusupov's social network"),r.a.createElement("p",null,"Click sign up to register")))}a(68);function _(){return r.a.createElement("div",{className:"NotFound"},r.a.createElement("h3",null,"Sorry, page not found!"))}var S=a(46),T=a(54),g=a(47),j=a(23),C=a(78),x=a(45),w=a.n(x);a(69);function k(e){var t=e.isLoading,a=e.className,n=void 0===a?"":a,c=e.disabled,o=void 0!==c&&c,l=Object(j.a)(e,["isLoading","className","disabled"]);return r.a.createElement(C.a,Object.assign({className:"LoaderButton ".concat(n),disabled:o||t},l),t&&r.a.createElement(w.a,{glyph:"refresh",className:"spinning"}),l.children)}var N=a(19),P=a(18);function y(e){var t=Object(n.useState)(e),a=Object(m.a)(t,2),r=a[0],c=a[1];return[r,function(e){c(Object(P.a)(Object(P.a)({},r),{},Object(N.a)({},e.target.id,e.target.value)))}]}a(70);function D(){var e=Object(h.k)(),t=d().setUserId,a=Object(n.useState)(!1),c=Object(m.a)(a,2),o=c[0],l=c[1],s=y({email:"",password:""}),p=Object(m.a)(s,2),E=p[0],f=p[1];function v(e){return b.apply(this,arguments)}function b(){return(b=Object(i.a)(u.a.mark((function e(t){var a;return u.a.wrap((function(e){for(;;)switch(e.prev=e.next){case 0:return e.next=2,t.text();case 2:if(a=e.sent,t.ok){e.next=9;break}if("Wrong credentials"!==a){e.next=8;break}throw a;case 8:throw Error(t.statusText);case 9:return e.abrupt("return",a);case 10:case"end":return e.stop()}}),e)})))).apply(this,arguments)}function _(){return(_=Object(i.a)(u.a.mark((function a(n){var r,c;return u.a.wrap((function(a){for(;;)switch(a.prev=a.next){case 0:return n.preventDefault(),l(!0),a.prev=2,(r=new FormData).append("login",E.email),r.append("password",E.password),a.next=8,fetch("http://localhost:".concat(Object({NODE_ENV:"production",PUBLIC_URL:"",WDS_SOCKET_HOST:void 0,WDS_SOCKET_PATH:void 0,WDS_SOCKET_PORT:void 0}).PORT,"/sign_in"),{method:"POST",body:r}).then(v);case 8:c=a.sent,t(c),e.push("/"),a.next=17;break;case 13:a.prev=13,a.t0=a.catch(2),O(a.t0),l(!1);case 17:case"end":return a.stop()}}),a,null,[[2,13]])})))).apply(this,arguments)}return r.a.createElement("div",{className:"Login"},r.a.createElement("form",{onSubmit:function(e){return _.apply(this,arguments)}},r.a.createElement(S.a,{controlId:"email"},r.a.createElement(T.a,null,"Email"),r.a.createElement(g.a,{autoFocus:!0,type:"email",value:E.email,onChange:f})),r.a.createElement(S.a,{controlId:"password"},r.a.createElement(T.a,null,"Password"),r.a.createElement(g.a,{type:"password",value:E.password,onChange:f})),r.a.createElement(k,{block:!0,type:"submit",isLoading:o,disabled:!(E.email.length>0&&E.password.length>0)},"Login")))}a(72);function L(){var e=Object(h.k)(),t=y({email:"",userName:"",password:"",confirmPassword:""}),a=Object(m.a)(t,2),c=a[0],o=a[1],l=Object(n.useState)(!1),s=Object(m.a)(l,2),p=s[0],d=s[1];function E(e){return f.apply(this,arguments)}function f(){return(f=Object(i.a)(u.a.mark((function e(t){var a;return u.a.wrap((function(e){for(;;)switch(e.prev=e.next){case 0:return e.next=2,t.text();case 2:if(a=e.sent,t.ok){e.next=9;break}if("User already exists"!==a){e.next=8;break}throw a;case 8:throw Error(t.statusText);case 9:return e.abrupt("return",t);case 10:case"end":return e.stop()}}),e)})))).apply(this,arguments)}function v(){return(v=Object(i.a)(u.a.mark((function t(a){var n;return u.a.wrap((function(t){for(;;)switch(t.prev=t.next){case 0:return a.preventDefault(),d(!0),t.prev=2,(n=new FormData).append("email",c.email),n.append("userName",c.userName),n.append("password",c.password),t.next=9,fetch("http://localhost:".concat(Object({NODE_ENV:"production",PUBLIC_URL:"",WDS_SOCKET_HOST:void 0,WDS_SOCKET_PATH:void 0,WDS_SOCKET_PORT:void 0}).PORT,"/create_user"),{method:"POST",body:n}).then(E);case 9:d(!1),e.push("/login"),t.next=17;break;case 13:t.prev=13,t.t0=t.catch(2),O(t.t0),d(!1);case 17:case"end":return t.stop()}}),t,null,[[2,13]])})))).apply(this,arguments)}return r.a.createElement("div",{className:"Signup"},r.a.createElement("form",{onSubmit:function(e){return v.apply(this,arguments)}},r.a.createElement(S.a,{controlId:"email"},r.a.createElement(T.a,null,"Email"),r.a.createElement(g.a,{autoFocus:!0,type:"email",value:c.email,onChange:o})),r.a.createElement(S.a,{controlId:"userName"},r.a.createElement(T.a,null,"User name"),r.a.createElement(g.a,{type:"text",value:c.userName,onChange:o})),r.a.createElement(S.a,{controlId:"password"},r.a.createElement(T.a,null,"Password"),r.a.createElement(g.a,{type:"password",value:c.password,onChange:o})),r.a.createElement(S.a,{controlId:"confirmPassword"},r.a.createElement(T.a,null,"Confirm Password"),r.a.createElement(g.a,{type:"password",onChange:o,value:c.confirmPassword})),r.a.createElement(k,{block:!0,type:"submit",isLoading:p,disabled:!(c.email.length>0&&c.userName.length>0&&c.password.length>0&&c.password===c.confirmPassword)},"Sign up")))}function W(e){var t=e.children,a=Object(j.a)(e,["children"]),n=Object(h.l)(),c=n.pathname,o=n.search,l=d().userId;return r.a.createElement(h.d,a,l?t:r.a.createElement(h.c,{to:"/login?redirect=".concat(c).concat(o)}))}function K(e){var t=e.children,a=Object(j.a)(e,["children"]),n=d().userId;return r.a.createElement(h.d,a,n?r.a.createElement(h.c,{to:"/"}):t)}var R=a(48),I=a(79),U=a(29);a(43);function H(){var e=Object(h.m)().id,t=d().userId,a=Object(h.k)(),c=Object(n.useState)(null),o=Object(m.a)(c,2),l=o[0],s=o[1],p=Object(n.useState)([]),E=Object(m.a)(p,2),f=E[0],b=E[1];function _(){return(_=Object(i.a)(u.a.mark((function a(){return u.a.wrap((function(a){for(;;)switch(a.prev=a.next){case 0:return a.prev=0,a.next=3,fetch("http://localhost:".concat(Object({NODE_ENV:"production",PUBLIC_URL:"",WDS_SOCKET_HOST:void 0,WDS_SOCKET_PATH:void 0,WDS_SOCKET_PORT:void 0}).PORT,"/add_friend/?user=").concat(t,"&friend=").concat(e)).then(v);case 3:a.next=8;break;case 5:a.prev=5,a.t0=a.catch(0),O(a.t0);case 8:window.location.reload(!1);case 9:case"end":return a.stop()}}),a,null,[[0,5]])})))).apply(this,arguments)}function S(){return(S=Object(i.a)(u.a.mark((function a(){return u.a.wrap((function(a){for(;;)switch(a.prev=a.next){case 0:return a.prev=0,a.next=3,fetch("http://localhost:".concat(Object({NODE_ENV:"production",PUBLIC_URL:"",WDS_SOCKET_HOST:void 0,WDS_SOCKET_PATH:void 0,WDS_SOCKET_PORT:void 0}).PORT,"/remove_friend/?user=").concat(t,"&friend=").concat(e)).then(v);case 3:a.next=8;break;case 5:a.prev=5,a.t0=a.catch(0),O(a.t0);case 8:window.location.reload(!1);case 9:case"end":return a.stop()}}),a,null,[[0,5]])})))).apply(this,arguments)}return Object(n.useEffect)((function(){function t(){return(t=Object(i.a)(u.a.mark((function t(){var a,n;return u.a.wrap((function(t){for(;;)switch(t.prev=t.next){case 0:return t.prev=0,t.next=3,fetch("http://localhost:".concat(Object({NODE_ENV:"production",PUBLIC_URL:"",WDS_SOCKET_HOST:void 0,WDS_SOCKET_PATH:void 0,WDS_SOCKET_PORT:void 0}).PORT,"/get_form/").concat(e)).then(v).then((function(e){return e.json()}));case 3:return a=t.sent,s(a),t.next=7,fetch("http://localhost:".concat(Object({NODE_ENV:"production",PUBLIC_URL:"",WDS_SOCKET_HOST:void 0,WDS_SOCKET_PATH:void 0,WDS_SOCKET_PORT:void 0}).PORT,"/get_friends/").concat(e)).then(v).then((function(e){return e.json()}));case 7:n=t.sent,b(n),t.next=14;break;case 11:t.prev=11,t.t0=t.catch(0),O(t.t0);case 14:case"end":return t.stop()}}),t,null,[[0,11]])})))).apply(this,arguments)}!function(){t.apply(this,arguments)}()}),[e]),r.a.createElement("div",{className:"form"},l&&r.a.createElement("div",null,r.a.createElement("h3",null,l.firstName," ",l.lastName),e===t?r.a.createElement(C.a,{onClick:function(){a.push("/edit_form/".concat(t))},className:"px-4"},"Edit form"):0===f.filter((function(t){return t.id!==e})).length?r.a.createElement(C.a,{variant:"success",onClick:function(){return function(){return _.apply(this,arguments)}()},className:"px-4"},"Add friend"):r.a.createElement(C.a,{variant:"danger",onClick:function(){return function(){return S.apply(this,arguments)}()},className:"px-4"},"Remove friend"),r.a.createElement("div",{class:"info"},r.a.createElement("div",{class:"left"},r.a.createElement("h2",null,r.a.createElement("strong",null,"Age")," ",l.age),r.a.createElement("h2",null,r.a.createElement("strong",null,"Gender")," ",l.gender),r.a.createElement("h2",null,r.a.createElement("strong",null,"Interests")," ",l.interests),r.a.createElement("h2",null,r.a.createElement("strong",null,"City")," ",l.city)),r.a.createElement("div",{class:"right"},r.a.createElement("h2",null,r.a.createElement("strong",null,"Friends"),r.a.createElement(I.a,{variant:"flush"},function(e){return[{}].concat(e).map((function(e,t){return 0!==t&&r.a.createElement(U.LinkContainer,{key:e.id,to:"/user_forms/".concat(e.id)},r.a.createElement(R.a,{action:!0},e.firstName," ",e.lastName))}))}(f)))))))}function B(){var e=Object(h.m)().id,t=d().userId,a=Object(h.k)(),c=Object(n.useState)(!1),o=Object(m.a)(c,2),l=o[0],s=o[1],p=function(e){var t=Object(n.useState)(e),a=Object(m.a)(t,2),r=a[0],c=a[1];return[r,function(e){c(e)},function(e){"age"===e.target.id?c(Object(P.a)(Object(P.a)({},r),{},Object(N.a)({},e.target.id,+e.target.value))):c(Object(P.a)(Object(P.a)({},r),{},Object(N.a)({},e.target.id,e.target.value)))}]}(null),E=Object(m.a)(p,3),f=E[0],b=E[1],_=E[2];function j(){return(j=Object(i.a)(u.a.mark((function t(n){return u.a.wrap((function(t){for(;;)switch(t.prev=t.next){case 0:return n.preventDefault(),s(!0),t.prev=2,t.next=5,fetch("http://localhost:".concat(Object({NODE_ENV:"production",PUBLIC_URL:"",WDS_SOCKET_HOST:void 0,WDS_SOCKET_PATH:void 0,WDS_SOCKET_PORT:void 0}).PORT,"/update_form/").concat(e),{method:"POST",headers:{"Content-Type":"application/json"},body:JSON.stringify(f)}).then(v);case 5:s(!1),a.push("/user_forms/".concat(e)),t.next=13;break;case 9:t.prev=9,t.t0=t.catch(2),O(t.t0),s(!1);case 13:case"end":return t.stop()}}),t,null,[[2,9]])})))).apply(this,arguments)}return Object(n.useEffect)((function(){function t(){return(t=Object(i.a)(u.a.mark((function t(){var a;return u.a.wrap((function(t){for(;;)switch(t.prev=t.next){case 0:return t.prev=0,t.next=3,fetch("http://localhost:".concat(Object({NODE_ENV:"production",PUBLIC_URL:"",WDS_SOCKET_HOST:void 0,WDS_SOCKET_PATH:void 0,WDS_SOCKET_PORT:void 0}).PORT,"/get_form/").concat(e)).then(v).then((function(e){return e.json()}));case 3:a=t.sent,b(a),t.next=10;break;case 7:t.prev=7,t.t0=t.catch(0),O(t.t0);case 10:case"end":return t.stop()}}),t,null,[[0,7]])})))).apply(this,arguments)}!function(){t.apply(this,arguments)}()}),[e]),r.a.createElement("div",{className:"Edit form"},f&&e===t&&r.a.createElement("form",{onSubmit:function(e){return j.apply(this,arguments)}},r.a.createElement(S.a,{controlId:"firstName"},r.a.createElement(T.a,null,"First name"),r.a.createElement(g.a,{autoFocus:!0,type:"text",value:f.firstName,onChange:_})),r.a.createElement(S.a,{controlId:"lastName"},r.a.createElement(T.a,null,"Last name"),r.a.createElement(g.a,{type:"text",value:f.lastName,onChange:_})),r.a.createElement(S.a,{controlId:"age"},r.a.createElement(T.a,null,"Age"),r.a.createElement(g.a,{type:"number",value:f.age,onChange:_})),r.a.createElement(S.a,{controlId:"gender"},r.a.createElement(T.a,null,"Gender"),r.a.createElement(g.a,{as:"select",value:f.gender,onChange:_},r.a.createElement("option",null),r.a.createElement("option",null,"M"),r.a.createElement("option",null,"F"))),r.a.createElement(S.a,{controlId:"interests"},r.a.createElement(T.a,null,"Interests"),r.a.createElement(g.a,{type:"text",value:f.interests,onChange:_})),r.a.createElement(S.a,{controlId:"city"},r.a.createElement(T.a,null,"City"),r.a.createElement(g.a,{type:"text",value:f.city,onChange:_})),r.a.createElement(k,{block:!0,type:"submit",isLoading:l},"Submit")))}var A=a(81);a(74);function V(){var e=Object(n.useState)([]),t=Object(m.a)(e,2),a=t[0],c=t[1],o=d().userId,l=Object(n.useState)(!0),s=Object(m.a)(l,2),p=s[0],E=s[1],f=Object(n.useState)(""),h=Object(m.a)(f,2),b=h[0],_=h[1];function S(e){_(e.target.value)}function T(e){return j.apply(this,arguments)}function j(){return(j=Object(i.a)(u.a.mark((function e(t){var a;return u.a.wrap((function(e){for(;;)switch(e.prev=e.next){case 0:if("Enter"!==t.key){e.next=12;break}return t.preventDefault(),e.prev=2,e.next=5,fetch("http://localhost:".concat(Object({NODE_ENV:"production",PUBLIC_URL:"",WDS_SOCKET_HOST:void 0,WDS_SOCKET_PATH:void 0,WDS_SOCKET_PORT:void 0}).PORT,"/all_users/?filter=").concat(b)).then(v).then((function(e){return e.json()}));case 5:a=e.sent,c(a),e.next=12;break;case 9:e.prev=9,e.t0=e.catch(2),O(e.t0);case 12:case"end":return e.stop()}}),e,null,[[2,9]])})))).apply(this,arguments)}return Object(n.useEffect)((function(){function e(){return(e=Object(i.a)(u.a.mark((function e(){var t;return u.a.wrap((function(e){for(;;)switch(e.prev=e.next){case 0:if(o){e.next=2;break}return e.abrupt("return");case 2:return e.prev=2,e.next=5,fetch("http://localhost:".concat(Object({NODE_ENV:"production",PUBLIC_URL:"",WDS_SOCKET_HOST:void 0,WDS_SOCKET_PATH:void 0,WDS_SOCKET_PORT:void 0}).PORT,"/all_users")).then(v).then((function(e){return e.json()}));case 5:t=e.sent,c(t),e.next=12;break;case 9:e.prev=9,e.t0=e.catch(2),O(e.t0);case 12:E(!1);case 13:case"end":return e.stop()}}),e,null,[[2,9]])})))).apply(this,arguments)}!function(){e.apply(this,arguments)}()}),[o]),r.a.createElement("div",{className:"Users"},o&&r.a.createElement("div",{className:"users"},r.a.createElement(A.a,{inline:!0},r.a.createElement(g.a,{type:"text",placeholder:"Search",className:"mr-sm-2",onChange:S,onKeyPress:T})),r.a.createElement(I.a,{variant:"flush"},!p&&function(e){return[{}].concat(e).map((function(e,t){return 0!==t&&r.a.createElement(U.LinkContainer,{key:e.id,to:"/user_forms/".concat(e.id)},r.a.createElement(R.a,{action:!0},e.firstName," ",e.lastName))}))}(a))))}function F(){return r.a.createElement(h.g,null,r.a.createElement(h.d,{exact:!0,path:"/"},r.a.createElement(b,null)),r.a.createElement(W,{exact:!0,path:"/users"},r.a.createElement(V,null)),r.a.createElement(W,{exact:!0,path:"/user_forms/:id"},r.a.createElement(H,null)),r.a.createElement(W,{exact:!0,path:"/edit_form/:id"},r.a.createElement(B,null)),r.a.createElement(W,{exact:!0,path:"/home"},r.a.createElement(b,null)),r.a.createElement(K,{exact:!0,path:"/login"},r.a.createElement(D,null)),r.a.createElement(K,{exact:!0,path:"/signup"},r.a.createElement(L,null)),r.a.createElement(h.d,null,r.a.createElement(_,null)))}a(75);var J=function(){var e=Object(h.k)(),t=Object(n.useState)(!0),a=Object(m.a)(t,2),c=a[0],o=a[1],l=Object(n.useState)(null),s=Object(m.a)(l,2),d=s[0],b=s[1];function _(e){return S.apply(this,arguments)}function S(){return(S=Object(i.a)(u.a.mark((function e(t){var a;return u.a.wrap((function(e){for(;;)switch(e.prev=e.next){case 0:return e.next=2,t.text();case 2:if(a=e.sent,t.ok){e.next=9;break}if("No current user"!==a){e.next=8;break}throw a;case 8:throw Error(t.statusText);case 9:return e.abrupt("return",a);case 10:case"end":return e.stop()}}),e)})))).apply(this,arguments)}function T(){return(T=Object(i.a)(u.a.mark((function t(){return u.a.wrap((function(t){for(;;)switch(t.prev=t.next){case 0:return t.next=2,fetch("http://localhost:".concat(Object({NODE_ENV:"production",PUBLIC_URL:"",WDS_SOCKET_HOST:void 0,WDS_SOCKET_PATH:void 0,WDS_SOCKET_PORT:void 0}).PORT,"/sign_out")).then(v);case 2:b(null),e.push("/login");case 4:case"end":return t.stop()}}),t)})))).apply(this,arguments)}return Object(n.useEffect)((function(){function e(){return(e=Object(i.a)(u.a.mark((function e(){var t;return u.a.wrap((function(e){for(;;)switch(e.prev=e.next){case 0:return e.prev=0,console.log(Object({NODE_ENV:"production",PUBLIC_URL:"",WDS_SOCKET_HOST:void 0,WDS_SOCKET_PATH:void 0,WDS_SOCKET_PORT:void 0})),e.next=4,fetch("http://localhost:".concat(Object({NODE_ENV:"production",PUBLIC_URL:"",WDS_SOCKET_HOST:void 0,WDS_SOCKET_PATH:void 0,WDS_SOCKET_PORT:void 0}).PORT,"/current_session")).then(_);case 4:t=e.sent,b(t),e.next=11;break;case 8:e.prev=8,e.t0=e.catch(0),"No current user"!==e.t0&&O(e.t0);case 11:o(!1);case 12:case"end":return e.stop()}}),e,null,[[0,8]])})))).apply(this,arguments)}!function(){e.apply(this,arguments)}()}),[]),!c&&r.a.createElement("div",{className:"App container"},r.a.createElement(E.a,{expand:"lg",bg:"dark",variant:"dark"},r.a.createElement(E.a.Brand,{href:"/"},r.a.createElement("h1",null,"Pavel's socials")),r.a.createElement(f.a,{className:"mr-auto"},d?r.a.createElement(r.a.Fragment,null,r.a.createElement(f.a.Link,{onClick:function(){e.push("/users")}},r.a.createElement("h3",null,"Search friends")),r.a.createElement(f.a.Link,{onClick:function(){return T.apply(this,arguments)}},r.a.createElement("h3",null,"Logout"))):r.a.createElement(r.a.Fragment,null,r.a.createElement(f.a.Link,{href:"login"},r.a.createElement("h3",null,"Login")),r.a.createElement(f.a.Link,{href:"signup"},r.a.createElement("h3",null,"Sign up"))))),r.a.createElement(p.Provider,{value:{userId:d,setUserId:b}},r.a.createElement(F,null)))};Boolean("localhost"===window.location.hostname||"[::1]"===window.location.hostname||window.location.hostname.match(/^127(?:\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}$/));o.a.render(r.a.createElement(l.BrowserRouter,null,r.a.createElement(J,null)),document.getElementById("root")),"serviceWorker"in navigator&&navigator.serviceWorker.ready.then((function(e){e.unregister()})).catch((function(e){console.error(e.message)}))}},[[55,1,2]]]);
//# sourceMappingURL=main.ed328a49.chunk.js.map