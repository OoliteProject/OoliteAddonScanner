<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="text"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:template match="/">
(
<xsl:for-each select="/catalog/manifest">
    <xsl:if test="position()>1">, </xsl:if>
    <xsl:apply-templates select="."/>
</xsl:for-each>)</xsl:template>

    <xsl:template match="manifest">  {
<xsl:for-each select="*">    "<xsl:value-of select="name()"/>" = "<xsl:value-of select="."/>";
</xsl:for-each>  }</xsl:template>
</xsl:stylesheet>
