<!--
  ~ Copyright 2010 the original author or authors.
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~      http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:d="http://docbook.org/ns/docbook"
                version="1.0">

  <xsl:param name="html.stylesheet">css/bootstrap/bootstrap.css</xsl:param>

  <!-- Customize the body attribute:
       - add onload for pretty printing if it is here
       - remove all other cssififed attributes (bgcolor, text, link, vlink, alink)
       - remove rtl handling (might be an issue though)
  -->
  <xsl:template name="body.attributes">
    <xsl:attribute name="onload">if (prettyPrint) { prettyPrint() }</xsl:attribute>
  </xsl:template>

  <!-- .book -> .container -->
  <xsl:template match="d:book" mode="class.value">
    <xsl:value-of select="'book container'"/>
  </xsl:template>

  <!-- Admonitions -->
  <xsl:template match="d:warning" mode="class.value">
    <xsl:value-of select="'alert-message block-message'"/>
  </xsl:template>
  <xsl:template match="d:note" mode="class.value">
    <xsl:value-of select="'success alert-message block-message'"/>
  </xsl:template>
  <xsl:template match="d:tip" mode="class.value">
    <xsl:value-of select="'info alert-message block-message'"/>
  </xsl:template>
  <xsl:template match="d:important" mode="class.value">
    <xsl:value-of select="'error alert-message block-message'"/>
  </xsl:template>
  <xsl:template match="d:caution" mode="class.value">
    <xsl:value-of select="'warning alert-message block-message'"/>
  </xsl:template>

  <xsl:template match="d:programlisting" mode="class.value">
    <xsl:value-of select="'prettyprint'"/>
  </xsl:template>

  <!-- When the language is specified add it to the class attribute -->
  <xsl:template name="apply-highlighting">
    <xsl:if test="string-length(normalize-space(@language)) > 0">
    <xsl:attribute name="class">prettyprint language-<xsl:value-of select="@language"/></xsl:attribute>
    </xsl:if>
    <xsl:apply-templates/>
  </xsl:template>

  <!-- Use an <h1> instead of <h2> for chapter titles and wrap with page-header to give more spacing -->
  <xsl:template name="component.title">
    <div class="page-header">
      <h1>
        <xsl:call-template name="anchor">
          <xsl:with-param name="node" select=".."/>
          <xsl:with-param name="conditional" select="0"/>
        </xsl:call-template>
        <xsl:apply-templates select=".." mode="object.title.markup"/>
      </h1>
    </div>
  </xsl:template>

</xsl:stylesheet>
