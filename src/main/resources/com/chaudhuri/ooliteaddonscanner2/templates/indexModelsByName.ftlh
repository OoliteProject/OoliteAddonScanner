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

        <h1>Models</h1>
        <table border="1">
            <tr>
                <th>Expansion</th>
                <th>Model file</th>
                <th>Vertexes</th>
                <th>Faces</th>
            </tr>
            <#list expansions as expansion>
                <#list expansion.models as modelname, model>
                    <tr>
                        <td>${expansion.name}</td>
                        <td>${modelname}</td>
                        <td>${model.nvertexes}</td>
                        <td>${model.nfaces}</td>
                    </tr>
                </#list>
            </#list>
        </table>
    </body>
</html>