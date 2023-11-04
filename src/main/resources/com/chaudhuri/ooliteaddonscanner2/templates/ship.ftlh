<html>
    <head>
        <title>Ship ${ name }</title>
        <link rel="stylesheet" href="../style.css">
    </head>
    <body>
        <table border="0" width="100%">
            <tr>
                <td>Back to <a href='../index.html'>Index</a></td>
                <td align="right"><small>Page generated: ${.now}</small></td>
            </tr>
        </table>

        <h1><img style="vertical-align: middle;" src="https://ooliteproject.github.io/oolite/oolite-icon.png"/> Ship ${ name }</h1>

        <table border='1'>
            <tr><td>Identifier</td><td>${ identifier }</td></tr>
            <tr><td>Name</td><td>${ name }</td></tr>
            <tr><td>Expansion</td><td><a href='../expansions/${ expansion }.html'>${ expansion.title }</a></td></tr>
        </table>
        <#if asWikipage??><p>More on <a href='${asWikipage}'>${asWikipage}</a></p></#if>
            
        <h2>Features</h2>
        <#if features?keys?has_content>
            <table border="1">
            <#list features?keys as key>
                <tr><td>${ key }</td><td>${ features[key] }</td></tr>
            </#list>
            </table>
        <#else>
            This ship has no features.
        </#if>
    </body>
</html>
