<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet 
 xmlns:xsl=
    "http://www.w3.org/1999/XSL/Transform" 
 version="1.0"
 >
    
    <xsl:template match="/">
        <html>
            <head>
                <title>catalog_html.xsl</title>
            </head>
            <body>
                <h1>Expansion List</h1>
                <h2>Content</h2>
                <ul>
                    <xsl:for-each select="/catalog/manifest">
                        <li>
                            <a>
                                <xsl:attribute name="href">#<xsl:value-of select="@identifier"/>@<xsl:value-of select="@version"/></xsl:attribute>
                                <xsl:value-of select="title"/> <xsl:value-of select="version"/>
                            </a>
                        </li>
                    </xsl:for-each>
                </ul>
                <xsl:apply-templates select="/catalog/manifest"/>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="manifest">
        <a>
            <xsl:attribute name="name"><xsl:value-of select="@identifier"/>@<xsl:value-of select="@version"/></xsl:attribute>
            <h2><xsl:value-of select="title"/></h2>
            <small><xsl:value-of select="@identifier"/>@<xsl:value-of select="@version"/></small>
            
            <table border="1">
                <tr>
                    <td>identifier</td>
                    <td><xsl:value-of select="@identifier"/></td>
                </tr>
                <tr>
                    <td>version</td>
                    <td><xsl:value-of select="@version"/></td>
                </tr>
                <xsl:for-each select="*">
                    <tr>
                        <td><xsl:value-of select="name()"/></td>
                        <td><xsl:value-of select="text()"/></td>
                    </tr>
                </xsl:for-each>
            </table>
        </a>
    </xsl:template>

</xsl:stylesheet>