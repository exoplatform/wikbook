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
<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:import href="urn:docbkx:stylesheet"/>

<!--
    <xsl:param name="html.stylesheet">css/bootstrap-1.4.0.min.css</xsl:param>
-->
  <xsl:param name="html.stylesheet">DUMMY</xsl:param>
    <xsl:template name="output.html.stylesheets">
        <link href="css/bootstrap/bootstrap.css" type="text/css" rel="stylesheet"/>
        <script src="js/bootstrap/jquery-1.7.1.min.js"></script>
        <script src="js/bootstrap/bootstrap-dropdown.js"></script>
        <script src="js/bootstrap/bootstrap-modal.js"></script>
        <script src="js/bootstrap/docbook.js"></script>
        <script src="js/bootstrap/google-code-prettify/prettify.js"></script>
    </xsl:template>

  <!-- Change list type for the TOC -->
  <xsl:param name="toc.list.type">ul</xsl:param>

  <!-- When the language is specifeid add it to the class attribute -->
  <xsl:template name="apply-highlighting">
    <xsl:if test="string-length(normalize-space(@language)) > 0">
    <xsl:attribute name="class">programlisting language-<xsl:value-of select="@language"/></xsl:attribute>
    </xsl:if>
    <xsl:apply-templates/>
  </xsl:template>

<!--
    <xsl:import href="urn:docbkx:stylesheet/chunkfast.xsl"/>
    <xsl:import href="htmlCommon.xsl"/>

    <xsl:param name="root.filename">guide</xsl:param>
    <xsl:param name="chunk.section.depth">0</xsl:param>
    <xsl:param name="chunk.quietly">1</xsl:param>
    <xsl:param name="use.id.as.filename">1</xsl:param>
-->

    <!-- HEADERS AND FOOTERS -->

    <!-- Use custom header -->
<!--
    <xsl:template name="header.navigation">
        <xsl:param name="next"/>
        <xsl:param name="prev"/>
        <xsl:if test=". != /book">
            <div class='navheader'>
                <xsl:call-template name="navlinks">
                    <xsl:with-param name="next" select="$next"/>
                    <xsl:with-param name="prev" select="$prev"/>
                </xsl:call-template>
            </div>
        </xsl:if>
    </xsl:template>
-->

    <!-- Use custom footer -->
<!--
    <xsl:template name="footer.navigation">
        <xsl:param name="next"/>
        <xsl:param name="prev"/>
        <div class='navfooter'>
            <xsl:call-template name="navlinks">
                <xsl:with-param name="next" select="$next"/>
                <xsl:with-param name="prev" select="$prev"/>
            </xsl:call-template>
        </div>
    </xsl:template>

    <xsl:template name="navlinks">
        <xsl:param name="next"/>
        <xsl:param name="prev"/>
        <div>
            <div class="navbar">
                <xsl:if test="count($prev)>0">
                    <xsl:call-template name="customXref">
                        <xsl:with-param name="target" select="$prev"/>
                        <xsl:with-param name="content">
                            <xsl:text>Previous</xsl:text>
                        </xsl:with-param>
                    </xsl:call-template>
                    <span>|</span>
                </xsl:if>
                <xsl:call-template name="customXref">
                    <xsl:with-param name="target" select="/book"/>
                    <xsl:with-param name="content">
                        <xsl:text>Contents</xsl:text>
                    </xsl:with-param>
                </xsl:call-template>
                <xsl:if test="count($next)>0">
                    <span>|</span>
                    <xsl:call-template name="customXref">
                        <xsl:with-param name="target" select="$next"/>
                        <xsl:with-param name="content">
                            <xsl:text>Next</xsl:text>
                        </xsl:with-param>
                    </xsl:call-template>
                </xsl:if>
            </div>
        </div>
    </xsl:template>
-->

</xsl:stylesheet>