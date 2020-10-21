(this["webpackJsonpauth-app-client"]=this["webpackJsonpauth-app-client"]||[]).push([[0],{43:function(e,t,a){},55:function(e,t,a){e.exports=a(76)},60:function(e,t,a){},62:function(e,t,a){},68:function(e,t,a){},69:function(e,t,a){},70:function(e,t,a){},72:function(e,t,a){},74:function(e,t,a){},75:function(e,t,a){},76:function(e,t,a){"use strict";a.r(t);var n=a(0),r=a.n(n),c=a(24),o=a.n(c),l=a(11),s=(a(60),a(5)),i=a.n(s),u=a(9),m=a(6),p=Object(n.createContext)(null);function f(){return Object(n.useContext)(p)}var d=a(80),h=a(82),E=a(7);function v(e){var t=e.toString();e instanceof Error||!e.message||(t=e.message),alert(t)}function b(e){if(!e.ok)throw Error(e.statusText);return e}a(62);function w(){console.log(window.location);var e=Object(E.k)(),t=f().userId;return r.a.createElement("div",{className:"Home"},t?void e.push("/user_forms/".concat(t)):r.a.createElement("div",{className:"lander"},r.a.createElement("h1",null,"Pavel Yusupov's social network"),r.a.createElement("p",null,"Click sign up to register")))}a(68);function g(){return r.a.createElement("div",{className:"NotFound"},r.a.createElement("h3",null,"Sorry, page not found!"))}var j=a(46),x=a(54),O=a(47),k=a(23),y=a(78),N=a(45),C=a.n(N);a(69);function S(e){var t=e.isLoading,a=e.className,n=void 0===a?"":a,c=e.disabled,o=void 0!==c&&c,l=Object(k.a)(e,["isLoading","className","disabled"]);return r.a.createElement(y.a,Object.assign({className:"LoaderButton ".concat(n),disabled:o||t},l),t&&r.a.createElement(C.a,{glyph:"refresh",className:"spinning"}),l.children)}var I=a(19),_=a(18);function L(e){var t=Object(n.useState)(e),a=Object(m.a)(t,2),r=a[0],c=a[1];return[r,function(e){c(Object(_.a)(Object(_.a)({},r),{},Object(I.a)({},e.target.id,e.target.value)))}]}a(70);function P(){var e=Object(E.k)(),t=f().setUserId,a=Object(n.useState)(!1),c=Object(m.a)(a,2),o=c[0],l=c[1],s=L({email:"",password:""}),p=Object(m.a)(s,2),d=p[0],h=p[1];function b(e){return w.apply(this,arguments)}function w(){return(w=Object(u.a)(i.a.mark((function e(t){var a;return i.a.wrap((function(e){for(;;)switch(e.prev=e.next){case 0:return e.next=2,t.text();case 2:if(a=e.sent,t.ok){e.next=9;break}if("Wrong credentials"!==a){e.next=8;break}throw a;case 8:throw Error(t.statusText);case 9:return e.abrupt("return",a);case 10:case"end":return e.stop()}}),e)})))).apply(this,arguments)}function g(){return(g=Object(u.a)(i.a.mark((function a(n){var r,c;return i.a.wrap((function(a){for(;;)switch(a.prev=a.next){case 0:return n.preventDefault(),l(!0),a.prev=2,(r=new FormData).append("login",d.email),r.append("password",d.password),a.next=8,fetch("".concat(window.location.origin,"/sign_in"),{method:"POST",body:r}).then(b);case 8:c=a.sent,t(c),e.push("/"),a.next=17;break;case 13:a.prev=13,a.t0=a.catch(2),v(a.t0),l(!1);case 17:case"end":return a.stop()}}),a,null,[[2,13]])})))).apply(this,arguments)}return r.a.createElement("div",{className:"Login"},r.a.createElement("form",{onSubmit:function(e){return g.apply(this,arguments)}},r.a.createElement(j.a,{controlId:"email"},r.a.createElement(x.a,null,"Email"),r.a.createElement(O.a,{autoFocus:!0,type:"email",value:d.email,onChange:h})),r.a.createElement(j.a,{controlId:"password"},r.a.createElement(x.a,null,"Password"),r.a.createElement(O.a,{type:"password",value:d.password,onChange:h})),r.a.createElement(S,{block:!0,type:"submit",isLoading:o,disabled:!(d.email.length>0&&d.password.length>0)},"Login")))}a(72);function F(){var e=Object(E.k)(),t=L({email:"",userName:"",password:"",confirmPassword:""}),a=Object(m.a)(t,2),c=a[0],o=a[1],l=Object(n.useState)(!1),s=Object(m.a)(l,2),p=s[0],f=s[1];function d(e){return h.apply(this,arguments)}function h(){return(h=Object(u.a)(i.a.mark((function e(t){var a;return i.a.wrap((function(e){for(;;)switch(e.prev=e.next){case 0:return e.next=2,t.text();case 2:if(a=e.sent,t.ok){e.next=9;break}if("User already exists"!==a){e.next=8;break}throw a;case 8:throw Error(t.statusText);case 9:return e.abrupt("return",t);case 10:case"end":return e.stop()}}),e)})))).apply(this,arguments)}function b(){return(b=Object(u.a)(i.a.mark((function t(a){var n;return i.a.wrap((function(t){for(;;)switch(t.prev=t.next){case 0:return a.preventDefault(),f(!0),t.prev=2,(n=new FormData).append("email",c.email),n.append("userName",c.userName),n.append("password",c.password),t.next=9,fetch("".concat(window.location.origin,"/create_user"),{method:"POST",body:n}).then(d);case 9:f(!1),e.push("/login"),t.next=17;break;case 13:t.prev=13,t.t0=t.catch(2),v(t.t0),f(!1);case 17:case"end":return t.stop()}}),t,null,[[2,13]])})))).apply(this,arguments)}return r.a.createElement("div",{className:"Signup"},r.a.createElement("form",{onSubmit:function(e){return b.apply(this,arguments)}},r.a.createElement(j.a,{controlId:"email"},r.a.createElement(x.a,null,"Email"),r.a.createElement(O.a,{autoFocus:!0,type:"email",value:c.email,onChange:o})),r.a.createElement(j.a,{controlId:"userName"},r.a.createElement(x.a,null,"User name"),r.a.createElement(O.a,{type:"text",value:c.userName,onChange:o})),r.a.createElement(j.a,{controlId:"password"},r.a.createElement(x.a,null,"Password"),r.a.createElement(O.a,{type:"password",value:c.password,onChange:o})),r.a.createElement(j.a,{controlId:"confirmPassword"},r.a.createElement(x.a,null,"Confirm Password"),r.a.createElement(O.a,{type:"password",onChange:o,value:c.confirmPassword})),r.a.createElement(S,{block:!0,type:"submit",isLoading:p,disabled:!(c.email.length>0&&c.userName.length>0&&c.password.length>0&&c.password===c.confirmPassword)},"Sign up")))}function T(e){var t=e.children,a=Object(k.a)(e,["children"]),n=Object(E.l)(),c=n.pathname,o=n.search,l=f().userId;return r.a.createElement(E.d,a,l?t:r.a.createElement(E.c,{to:"/login?redirect=".concat(c).concat(o)}))}function D(e){var t=e.children,a=Object(k.a)(e,["children"]),n=f().userId;return r.a.createElement(E.d,a,n?r.a.createElement(E.c,{to:"/"}):t)}var B=a(48),U=a(79),A=a(29);a(43);function J(){var e=Object(E.m)().id,t=f().userId,a=Object(E.k)(),c=Object(n.useState)(null),o=Object(m.a)(c,2),l=o[0],s=o[1],p=Object(n.useState)([]),d=Object(m.a)(p,2),h=d[0],w=d[1];function g(){return(g=Object(u.a)(i.a.mark((function a(){return i.a.wrap((function(a){for(;;)switch(a.prev=a.next){case 0:return a.prev=0,a.next=3,fetch("".concat(window.location.origin,"/add_friend/?user=").concat(t,"&friend=").concat(e)).then(b);case 3:a.next=8;break;case 5:a.prev=5,a.t0=a.catch(0),v(a.t0);case 8:window.location.reload(!1);case 9:case"end":return a.stop()}}),a,null,[[0,5]])})))).apply(this,arguments)}function j(){return(j=Object(u.a)(i.a.mark((function a(){return i.a.wrap((function(a){for(;;)switch(a.prev=a.next){case 0:return a.prev=0,a.next=3,fetch("".concat(window.location.origin,"/remove_friend/?user=").concat(t,"&friend=").concat(e)).then(b);case 3:a.next=8;break;case 5:a.prev=5,a.t0=a.catch(0),v(a.t0);case 8:window.location.reload(!1);case 9:case"end":return a.stop()}}),a,null,[[0,5]])})))).apply(this,arguments)}return Object(n.useEffect)((function(){function t(){return(t=Object(u.a)(i.a.mark((function t(){var a,n;return i.a.wrap((function(t){for(;;)switch(t.prev=t.next){case 0:return t.prev=0,t.next=3,fetch("".concat(window.location.origin,"/get_form/").concat(e)).then(b).then((function(e){return e.json()}));case 3:return a=t.sent,s(a),t.next=7,fetch("".concat(window.location.origin,"/get_friends/").concat(e)).then(b).then((function(e){return e.json()}));case 7:n=t.sent,w(n),t.next=14;break;case 11:t.prev=11,t.t0=t.catch(0),v(t.t0);case 14:case"end":return t.stop()}}),t,null,[[0,11]])})))).apply(this,arguments)}!function(){t.apply(this,arguments)}()}),[e]),r.a.createElement("div",{className:"form"},l&&r.a.createElement("div",null,r.a.createElement("h3",null,l.firstName," ",l.lastName),e===t?r.a.createElement(y.a,{onClick:function(){a.push("/edit_form/".concat(t))},className:"px-4"},"Edit form"):0===h.filter((function(t){return t.id!==e})).length?r.a.createElement(y.a,{variant:"success",onClick:function(){return function(){return g.apply(this,arguments)}()},className:"px-4"},"Add friend"):r.a.createElement(y.a,{variant:"danger",onClick:function(){return function(){return j.apply(this,arguments)}()},className:"px-4"},"Remove friend"),r.a.createElement("div",{class:"info"},r.a.createElement("div",{class:"left"},r.a.createElement("h2",null,r.a.createElement("strong",null,"Age")," ",l.age),r.a.createElement("h2",null,r.a.createElement("strong",null,"Gender")," ",l.gender),r.a.createElement("h2",null,r.a.createElement("strong",null,"Interests")," ",l.interests),r.a.createElement("h2",null,r.a.createElement("strong",null,"City")," ",l.city)),r.a.createElement("div",{class:"right"},r.a.createElement("h2",null,r.a.createElement("strong",null,"Friends"),r.a.createElement(U.a,{variant:"flush"},function(e){return[{}].concat(e).map((function(e,t){return 0!==t&&r.a.createElement(A.LinkContainer,{key:e.id,to:"/user_forms/".concat(e.id)},r.a.createElement(B.a,{action:!0},e.firstName," ",e.lastName))}))}(h)))))))}function W(){var e=Object(E.m)().id,t=f().userId,a=Object(E.k)(),c=Object(n.useState)(!1),o=Object(m.a)(c,2),l=o[0],s=o[1],p=function(e){var t=Object(n.useState)(e),a=Object(m.a)(t,2),r=a[0],c=a[1];return[r,function(e){c(e)},function(e){"age"===e.target.id?c(Object(_.a)(Object(_.a)({},r),{},Object(I.a)({},e.target.id,+e.target.value))):c(Object(_.a)(Object(_.a)({},r),{},Object(I.a)({},e.target.id,e.target.value)))}]}(null),d=Object(m.a)(p,3),h=d[0],w=d[1],g=d[2];function k(){return(k=Object(u.a)(i.a.mark((function t(n){return i.a.wrap((function(t){for(;;)switch(t.prev=t.next){case 0:return n.preventDefault(),s(!0),t.prev=2,t.next=5,fetch("".concat(window.location.origin,"/update_form/").concat(e),{method:"POST",headers:{"Content-Type":"application/json"},body:JSON.stringify(h)}).then(b);case 5:s(!1),a.push("/user_forms/".concat(e)),t.next=13;break;case 9:t.prev=9,t.t0=t.catch(2),v(t.t0),s(!1);case 13:case"end":return t.stop()}}),t,null,[[2,9]])})))).apply(this,arguments)}return Object(n.useEffect)((function(){function t(){return(t=Object(u.a)(i.a.mark((function t(){var a;return i.a.wrap((function(t){for(;;)switch(t.prev=t.next){case 0:return t.prev=0,t.next=3,fetch("".concat(window.location.origin,"/get_form/").concat(e)).then(b).then((function(e){return e.json()}));case 3:a=t.sent,w(a),t.next=10;break;case 7:t.prev=7,t.t0=t.catch(0),v(t.t0);case 10:case"end":return t.stop()}}),t,null,[[0,7]])})))).apply(this,arguments)}!function(){t.apply(this,arguments)}()}),[e]),r.a.createElement("div",{className:"Edit form"},h&&e===t&&r.a.createElement("form",{onSubmit:function(e){return k.apply(this,arguments)}},r.a.createElement(j.a,{controlId:"firstName"},r.a.createElement(x.a,null,"First name"),r.a.createElement(O.a,{autoFocus:!0,type:"text",value:h.firstName,onChange:g})),r.a.createElement(j.a,{controlId:"lastName"},r.a.createElement(x.a,null,"Last name"),r.a.createElement(O.a,{type:"text",value:h.lastName,onChange:g})),r.a.createElement(j.a,{controlId:"age"},r.a.createElement(x.a,null,"Age"),r.a.createElement(O.a,{type:"number",value:h.age,onChange:g})),r.a.createElement(j.a,{controlId:"gender"},r.a.createElement(x.a,null,"Gender"),r.a.createElement(O.a,{as:"select",value:h.gender,onChange:g},r.a.createElement("option",null),r.a.createElement("option",null,"M"),r.a.createElement("option",null,"F"))),r.a.createElement(j.a,{controlId:"interests"},r.a.createElement(x.a,null,"Interests"),r.a.createElement(O.a,{type:"text",value:h.interests,onChange:g})),r.a.createElement(j.a,{controlId:"city"},r.a.createElement(x.a,null,"City"),r.a.createElement(O.a,{type:"text",value:h.city,onChange:g})),r.a.createElement(S,{block:!0,type:"submit",isLoading:l},"Submit")))}var G=a(81);a(74);function R(){var e=Object(n.useState)([]),t=Object(m.a)(e,2),a=t[0],c=t[1],o=f().userId,l=Object(n.useState)(!0),s=Object(m.a)(l,2),p=s[0],d=s[1],h=Object(n.useState)(""),E=Object(m.a)(h,2),w=E[0],g=E[1];function j(e){g(e.target.value)}function x(e){return k.apply(this,arguments)}function k(){return(k=Object(u.a)(i.a.mark((function e(t){var a;return i.a.wrap((function(e){for(;;)switch(e.prev=e.next){case 0:if("Enter"!==t.key){e.next=12;break}return t.preventDefault(),e.prev=2,e.next=5,fetch("".concat(window.location.origin,"/all_users/?filter=").concat(w)).then(b).then((function(e){return e.json()}));case 5:a=e.sent,c(a),e.next=12;break;case 9:e.prev=9,e.t0=e.catch(2),v(e.t0);case 12:case"end":return e.stop()}}),e,null,[[2,9]])})))).apply(this,arguments)}return Object(n.useEffect)((function(){function e(){return(e=Object(u.a)(i.a.mark((function e(){var t;return i.a.wrap((function(e){for(;;)switch(e.prev=e.next){case 0:if(o){e.next=2;break}return e.abrupt("return");case 2:return e.prev=2,e.next=5,fetch("".concat(window.location.origin,"/all_users")).then(b).then((function(e){return e.json()}));case 5:t=e.sent,c(t),e.next=12;break;case 9:e.prev=9,e.t0=e.catch(2),v(e.t0);case 12:d(!1);case 13:case"end":return e.stop()}}),e,null,[[2,9]])})))).apply(this,arguments)}!function(){e.apply(this,arguments)}()}),[o]),r.a.createElement("div",{className:"Users"},o&&r.a.createElement("div",{className:"users"},r.a.createElement(G.a,{inline:!0},r.a.createElement(O.a,{type:"text",placeholder:"Search",className:"mr-sm-2",onChange:j,onKeyPress:x})),r.a.createElement(U.a,{variant:"flush"},!p&&function(e){return[{}].concat(e).map((function(e,t){return 0!==t&&r.a.createElement(A.LinkContainer,{key:e.id,to:"/user_forms/".concat(e.id)},r.a.createElement(B.a,{action:!0},e.firstName," ",e.lastName))}))}(a))))}function H(){return console.log(window.location),r.a.createElement(E.g,null,r.a.createElement(E.d,{exact:!0,path:"/"},r.a.createElement(w,null)),r.a.createElement(T,{exact:!0,path:"/users"},r.a.createElement(R,null)),r.a.createElement(T,{exact:!0,path:"/user_forms/:id"},r.a.createElement(J,null)),r.a.createElement(T,{exact:!0,path:"/edit_form/:id"},r.a.createElement(W,null)),r.a.createElement(T,{exact:!0,path:"/home"},r.a.createElement(w,null)),r.a.createElement(D,{exact:!0,path:"/login"},r.a.createElement(P,null)),r.a.createElement(D,{exact:!0,path:"/signup"},r.a.createElement(F,null)),r.a.createElement(E.d,null,r.a.createElement(g,null)))}a(75);var K=function(){var e=Object(E.k)(),t=Object(n.useState)(!0),a=Object(m.a)(t,2),c=a[0],o=a[1],l=Object(n.useState)(null),s=Object(m.a)(l,2),f=s[0],w=s[1];function g(e){return j.apply(this,arguments)}function j(){return(j=Object(u.a)(i.a.mark((function e(t){var a;return i.a.wrap((function(e){for(;;)switch(e.prev=e.next){case 0:return e.next=2,t.text();case 2:if(a=e.sent,t.ok){e.next=9;break}if("No current user"!==a){e.next=8;break}throw a;case 8:throw Error(t.statusText);case 9:return e.abrupt("return",a);case 10:case"end":return e.stop()}}),e)})))).apply(this,arguments)}function x(){return(x=Object(u.a)(i.a.mark((function t(){return i.a.wrap((function(t){for(;;)switch(t.prev=t.next){case 0:return t.next=2,fetch("".concat(window.location.origin,"/sign_out")).then(b);case 2:w(null),e.push("/login");case 4:case"end":return t.stop()}}),t)})))).apply(this,arguments)}return Object(n.useEffect)((function(){function e(){return(e=Object(u.a)(i.a.mark((function e(){var t;return i.a.wrap((function(e){for(;;)switch(e.prev=e.next){case 0:return e.prev=0,console.log(window.location),e.next=4,fetch("".concat(window.location.origin,"/current_session")).then(g);case 4:t=e.sent,w(t),e.next=11;break;case 8:e.prev=8,e.t0=e.catch(0),"No current user"!==e.t0&&v(e.t0);case 11:o(!1);case 12:case"end":return e.stop()}}),e,null,[[0,8]])})))).apply(this,arguments)}!function(){e.apply(this,arguments)}()}),[]),!c&&r.a.createElement("div",{className:"App container"},r.a.createElement(d.a,{expand:"lg",bg:"dark",variant:"dark"},r.a.createElement(d.a.Brand,{href:"/"},r.a.createElement("h1",null,"Pavel's socials")),r.a.createElement(h.a,{className:"mr-auto"},f?r.a.createElement(r.a.Fragment,null,r.a.createElement(h.a.Link,{onClick:function(){e.push("/users")}},r.a.createElement("h3",null,"Search friends")),r.a.createElement(h.a.Link,{onClick:function(){return x.apply(this,arguments)}},r.a.createElement("h3",null,"Logout"))):r.a.createElement(r.a.Fragment,null,r.a.createElement(h.a.Link,{href:"login"},r.a.createElement("h3",null,"Login")),r.a.createElement(h.a.Link,{href:"signup"},r.a.createElement("h3",null,"Sign up"))))),r.a.createElement(p.Provider,{value:{userId:f,setUserId:w}},r.a.createElement(H,null)))};Boolean("localhost"===window.location.hostname||"[::1]"===window.location.hostname||window.location.hostname.match(/^127(?:\.(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}$/));console.log(window.location),o.a.render(r.a.createElement(l.BrowserRouter,null,r.a.createElement(K,null)),document.getElementById("root")),"serviceWorker"in navigator&&navigator.serviceWorker.ready.then((function(e){e.unregister()})).catch((function(e){console.error(e.message)}))}},[[55,1,2]]]);
//# sourceMappingURL=main.a818c296.chunk.js.map