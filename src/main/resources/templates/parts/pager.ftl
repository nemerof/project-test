<#macro pager url page>
    <#if page.getTotalPages() gt 10>
        <#assign
        totalPages = page.getTotalPages()
        pageNumber = page.getNumber() + 1

        first = (pageNumber > 5)?then([1, -1], [1, 2, 3, 4, 5])
        first = (pageNumber == 4)?then([1, 2, 3, 4, 5, 6], first)
        last = (pageNumber < totalPages - 3)?then([-1, totalPages], [totalPages - 2, totalPages - 1, totalPages])
        bodyBefore = (pageNumber > 5 && pageNumber < totalPages - 1)?then([pageNumber - 2, pageNumber - 1], [])
        bodyAfter = (pageNumber > 4 && pageNumber < totalPages - 3)?then([pageNumber + 1, pageNumber + 2], [])

        body = first + bodyBefore + (pageNumber > 5 && pageNumber < totalPages - 2)?then([pageNumber], []) + bodyAfter + last
        >
    <#else>
       <#assign body = (page.getTotalPages() == 0)?then([1], 1..page.getTotalPages())>
    </#if>
  <div class="mt-3">
    <ul class="pagination d-flex justify-content-center">
      <li class="page-item disabled">
        <a class="page-link" href="#" tabindex="-1">Page</a>
      </li>
        <#list body as p>
            <#if (p - 1) == page.getNumber()>
              <li class="page-item active">
                <a class="page-link" href="#" tabindex="-1">${p}</a>
              </li>
            <#elseif p == -1>
              <li class="page-item disabled">
                <a class="page-link" href="#" tabindex="-1">...</a>
              </li>
            <#else>
              <li class="page-item">
<#--                <a class="page-link" href="${url}?page=${p - 1}&size=${page.getSize()}" tabindex="-1">${p}</a>-->
              </li>
            </#if>
        </#list>
    </ul>

    <ul class="pagination d-flex justify-content-center">
      <li class="page-item disabled">
        <a class="page-link" href="#" tabindex="-1">Messages on page</a>
      </li>
        <#list [1, 5, 10, 25, 50] as c>
            <#if c == page.getSize()>
              <li class="page-item active">
                <a class="page-link" href="#" tabindex="-1">${c}</a>
              </li>
            <#else>
              <li class="page-item">
<#--                <a class="page-link" href="${url}?page=${page.getNumber()}&size=${c}" tabindex="-1">${c}</a>-->
              </li>
            </#if>
        </#list>
    </ul>
  </div>
</#macro>