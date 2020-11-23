<#assign
known = Session.SPRING_SECURITY_CONTEXT??
>

<#if known>
    <#assign
    user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
    userId = user.getId()
    name = user.getUsername()
    isAuthorized = user.isAuthorized()
    isAdmin = user.isAdmin()
    >
<#else>
    <#assign
    userId ="-1"
    name = ""
    isAuthorized = false
    isAdmin = false
    >
</#if>