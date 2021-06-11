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

        <h1>Equipment by Name</h1>
        ${ equipment?size } items
        <table border="1">
            <tr>
                <th>Equipment</th>
                <th>Expansion</th>
                <th>Remarks</th>
            </tr>
            <#list equipmentByName as equipment>
                <tr>
                    <td><a href="equipment/${ equipment.identifier }.html">${ equipment.name }</a></td>
                    <td><a href="expansions/${ equipment.expansion.identifier }.html">${ equipment.expansion.name }</a></td>
                    <td>
                        <#if equipment.visible>
                            <#if equipment.asWikipage??><a target="_blank" href="${ equipment.asWikipage }">Wiki Page</a><#else><font color="red">No Wiki</font></#if>
                        <#else>
                            Not visible
                        </#if>
                    </td>
                </tr>
            </#list>
        </table>
    </body>
</html>