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

        <h1><img style="vertical-align: middle;" src="https://ooliteproject.github.io/oolite/oolite-icon.png"/> Expansions by Upload Date</h1>

        ${ expansions?size } items
        <table border="1">
            <tr>
                <th>Expansion</th>
                <th>Category</th>
                <th>Author</th>
                <th>Version</th>
                <th>Upload Date</th>
                <th>Remarks</th>
            </tr>
            <#list expansionsByUploadDate as expansion>
                <tr>
                    <td><a href="expansions/${ expansion.identifier }.html">${ expansion.title }</a></td>
                    <td><#if expansion.category??>${ expansion.category }</#if></td>
                    <td><#if expansion.author??>${ expansion.author }</#if></td>
                    <td><#if expansion.version??>${ expansion.version }</#if></td>
                    <td><#if expansion.uploadDate??>${ expansion.uploadDateAsInstant?datetime.iso?string["y-M-d H:m"] }</#if></td>
                    <td><#if expansion.asWikipage??><a target="_blank" href="${ expansion.asWikipage }">Wiki Page</a><#else><font color="red">No Wiki</font></#if> <#if expansion.warnings?has_content><font color="orange">Warnings</font></#if></td>
                </tr>
            </#list>
        </table>
    </body>
</html>