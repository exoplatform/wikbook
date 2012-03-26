<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:d="http://docbook.org/ns/docbook"
                exclude-result-prefixes="d"
                version="1.0">

  <xsl:import href="urn:docbkx:stylesheet/chunkfast.xsl"/>
  <xsl:import href="common.xsl"/>

  <!-- Chunker configuration -->
  <xsl:param name="chunk.section.depth">0</xsl:param>
  <xsl:param name="chunk.quietly">1</xsl:param>
  <xsl:param name="use.id.as.filename">1</xsl:param>

  <!-- We need the doctype -->
  <xsl:param name="chunker.output.doctype-system">html</xsl:param>

  <!-- -->
  <xsl:template name="user.head.content">
    <script src="js/bootstrap/google-code-prettify/prettify.js"></script>
  </xsl:template>

  <!-- .book -> .container -->
  <xsl:template match="d:chapter" mode="class.value">
    <xsl:value-of select="'chapter container'"/>
  </xsl:template>

</xsl:stylesheet>