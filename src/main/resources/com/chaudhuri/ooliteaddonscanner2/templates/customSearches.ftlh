<html>
    <head>
        <title>Search Results</title>
        <link rel="stylesheet" href="../style.css">
    </head>
    <body>
        <table border="0" width="100%">
            <tr>
                <td>Back to <a href='../index.html'>Index</a></td>
                <td align="right"><small>Page generated: ${.now}</small></td>
            </tr>
        </table>

        <h1>Search Results</h1>
        <#if searches?has_content>
            <#list searches as s>
                <h2>${ s.name }</h2>
                <table border="1">
                    <tr>
                        <td>Pattern</td>
                        <td>${ s.pattern }</td>
                    </tr>
                    <tr>
                        <td>Include</td>
                        <td>${ s.includePattern! }</td>
                    </tr>
                    <tr>
                        <td>Exclude</td>
                        <td>${ s.excludePattern! }</td>
                    </tr>
                </table>
                <#if s.results?has_content>
                    <p>${ s.results?size } Hits</p>
                    <#list s.results as h>
                        <p>
                        ${ h.expansionId! } ${ h.relPath! } : ${ h.lineNumber! }<br/>
                        <tt>${ h.leader }<font color="red">${ h.match }</font>${ h.trailer }</tt><br/>
                        </p>
                    </#list>
                <#else>
                    No hits.
                </#if>
            </#list>
        </#if>
    </body>
</html>
