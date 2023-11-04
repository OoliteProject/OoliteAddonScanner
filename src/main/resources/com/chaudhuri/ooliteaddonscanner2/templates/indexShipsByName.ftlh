<html>
    <head>
        <title>Oolite Known Universe</title>
        <link rel="stylesheet" href="style.css">
    </head>
    <body>
        <table border="0" width="100%">
            <tr>
                <td>Back to <a href='index.html'>Index</a></td>
                <td align="right"><small>Page generated: ${.now}</small></td>
            </tr>
        </table>

        <h1><img style="vertical-align: middle;" src="https://ooliteproject.github.io/oolite/oolite-icon.png"/> Ships</h1>

        ${ ships?size } items
        <table border="1">
            <tr>
                <th>Ship</th>
                <th>Expansion</th>
                <th>Roles</th>
                <th>Remarks</th>
            </tr>
            <#list shipsByName as ship>
                <tr>
                    <td><a href="ships/${ ship.identifier }.html">${ ship.name }</a></td>
                    <td><a href="expansions/${ ship.expansion.identifier }.html">${ ship.expansion.name }</a></td>
                    <td>${ ship.features.roles!"" }</td>
                    <td>
                        <#if ship.template>
                            Template
                        <#else>
                            <#if ship.asWikipage??><a target="_blank" href="${ ship.asWikipage }">Wiki Page</a><#else><font color="red">No Wiki</font></#if>
                        </#if>
                    </td>
                </tr>
            </#list>
        </table>
    </body>
</html>