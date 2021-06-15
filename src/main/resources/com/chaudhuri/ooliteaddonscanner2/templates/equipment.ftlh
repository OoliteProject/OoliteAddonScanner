<html>
    <head>
        <title>Equipment ${ name }</title>
        <link rel="stylesheet" href="../style.css">
    </head>
    <body>
        <table border="0" width="100%">
            <tr>
                <td>Back to <a href='../index.html'>Index</a></td>
                <td align="right"><small>Page generated: ${.now}</small></td>
            </tr>
        </table>

        <h1>Equipment ${ name }</h1>
        <p>${ description }</p>
        <#if asWikipage??><p>More on <a href='${asWikipage}'>${asWikipage}</a></p></#if>

        <table border='1'>
            <tr><td>Identifier</td><td>${ identifier }</td></tr>
            <tr><td>Name</td><td>${ name }</td></tr>
            <tr><td>Cost</td><td>${ cost } deci-credits (divide by 10 to get credits)</td></tr>
            <tr><td>Tech Level</td><td>${ techlevel } (available on ${ techlevel?number + 1 }+)</td></tr>
            <tr><td>Expansion</td><td><a href='../expansions/${ expansion }.html'>${ expansion.title }</a></td></tr>
        </table>
            
        <h2>Features</h2>
        <#if features?keys?has_content>
            <table border="1">
            <#list features?keys as key>
                <tr><td>${ key }</td><td>${ features[key] }</td></tr>
            </#list>
            </table>
        <#else>
            This Equipment has no features.
        </#if>
    </body>
</html>
