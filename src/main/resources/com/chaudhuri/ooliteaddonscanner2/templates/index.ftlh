<html>
    <head>
        <title>Oolite Known Universe</title>
        <link rel="stylesheet" href="style.css">
    </head>
    <body>
        <table border="0" width="100%">
            <tr>
                <td><a target="_blank" href="http://oolite.space/">Oolite Homepage</a></td>
                <td><a target="_blank" href="http://aegidian.org/bb/">Oolite Forum</a></td>
                <td><a target="_blank" href="http://wiki.alioth.net/index.php/Oolite_Main_Page">Oolite Wiki</a></td>
                <td align="right"><small>Page generated: ${.now}<br/>${ properties.ImplementationTitle } ${ properties.ImplementationVersion }</small></td>
            </tr>
        </table>

        <h1>Oolite Known Universe</h1>

        <ul>
            <li><a href="indexExpansionsByName.html">${ expansions?size } Expansions by Name</a></li>
            <li><a href="indexEquipmentByName.html">${ equipment?size } Equipment by Name</a></li>
            <li><a href="indexShipsByName.html">${ ships?size } Ships by Name</a></li>
            <li><a href="indexAllByIdentifier.html">All by Identifier</a></li>
            <li><a href="indexModelsByName.html">Models</a></li>
            <li><a href="warnings.html">${ warnings?size } Warnings</a></li>
        </ul>

        <p>
            Oolite was downloaded from <a href="${ properties.ooliteDownloadUrl }">${ properties.ooliteDownloadUrl }</a><br/>
            Expansion Manager Manifest was downloaded from <a href="${ properties.expansionManagerUrl }">${ properties.expansionManagerUrl }</a>
        </p>
    </body>
</html>