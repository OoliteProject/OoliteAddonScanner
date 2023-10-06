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
</xsl:for-each>
)</xsl:template>

    <xsl:template match="manifest">  {
    "identifier" = "<xsl:value-of select="@identifier"/>";
    "version" = "<xsl:value-of select="@version"/>";
<xsl:apply-templates select="*">
        <xsl:with-param name="nesting"><xsl:text>    </xsl:text></xsl:with-param>
    </xsl:apply-templates>
  }</xsl:template>
  
  <xsl:template match="requires_oxps|optional_oxps|conflict_oxps">
      <xsl:param name="nesting"/><xsl:value-of select="$nesting"/>"<xsl:value-of select="name()"/>" = (
<xsl:for-each select="*"><xsl:if test="position()>1">,
</xsl:if><xsl:apply-templates select=".">
          <xsl:with-param name="nesting"><xsl:value-of select="$nesting"/><xsl:text>    </xsl:text></xsl:with-param>
      </xsl:apply-templates></xsl:for-each><xsl:text>
</xsl:text><xsl:value-of select="$nesting"/>);
</xsl:template>

  <xsl:template match="dependency">
    <xsl:param name="nesting"/><xsl:value-of select="$nesting"/>{
<xsl:apply-templates select="*">
    <xsl:with-param name="nesting"><xsl:value-of select="$nesting"/><xsl:text>  </xsl:text></xsl:with-param>
</xsl:apply-templates>
<xsl:value-of select="$nesting"/>}</xsl:template>

  <!-- fields that are rendered without quoted values -->
  <xsl:template match="upload_date|file_size">
      <xsl:param name="nesting"/>
<xsl:value-of select="$nesting"/>"<xsl:value-of select="name()"/>" = <xsl:value-of select="."/>;
</xsl:template>

  <!-- fields that are rendered as list -->
  <xsl:template match="tags">
      <xsl:param name="nesting"/>
<xsl:value-of select="$nesting"/>"<xsl:value-of select="name()"/>" = ("<xsl:value-of select="tag" separator="&quot;, &quot;"/>");
</xsl:template>

  <!-- fields that are rendered with quoted values -->
  <xsl:template match="*">
      <xsl:param name="nesting"/>
<xsl:value-of select="$nesting"/>"<xsl:value-of select="name()"/>" = "<xsl:value-of select="."/>";
</xsl:template>

</xsl:stylesheet>
