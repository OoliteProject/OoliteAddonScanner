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

        <h1>Warnings</h1>
        <#if warnings?has_content>
            <h3>Global</h3>
            <ul>
                <#list globalWarnings as w>
                    <li> ${ w } </li>
                </#list>
            </ul>
        </#if>
        <#list expansions as expansion>
            <#if expansion.warnings?has_content>
                <h3><a href="expansions/${ expansion.identifier }.html">${ expansion.title }</a></h3>
                <ul>
                    <#list expansion.warnings as w>
                        <li> ${ w } </li>
                    </#list>
                </ul>
            </#if>
        </#list>
    </body>
</html>