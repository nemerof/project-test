

<#macro profilePicture picture width height>
<#-- <#if !(profilePic == "default-profile-icon.png")>-->
  <img src="/img/${picture}" class="rounded" alt="No pic :(" width="${width}" height="${height}"/>
<#-- <#else>
  <img src="/static/images/default-profile-icon.png" class="rounded" alt="No pic :(" width="${width}" height="${height}">
    </#if> -->
</#macro>