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

        <h1>All by Identifier</h1>
        ${ allByIdentifier?size } items
        <table border="1">
            <tr>
                <th>Type</th>
                <th>Identifier</th>
                <th>Name</th>
                <th>Expansion</th>
                <th>Remarks</th>
            </tr>
            <#list allByIdentifier as ww>
                <#assign type><#if ww.type == "Ship">ships</#if><#if ww.type == "Expansion">expansions</#if><#if ww.type == "Equipment">equipment</#if></#assign>
                <tr><td>${ww.type}</td><td><a href="${ type }/${ ww.identifier }.html">${ ww.identifier }</a></td><td><a href="${ type }/${ ww.identifier }.html">${ ww.name }</a></td><td><#if ww.expansion??><a href="expansions/${ ww.expansion.identifier }.html">${ ww.expansion.name }</a></#if></td><td><#if ww.asWikipage??><a target="_blank" href="${ ww.asWikipage }">Wiki Page</a><#else><font color="red">No Wiki</font></#if></td></tr>
            </#list>
        </table>
    </body>
</html>