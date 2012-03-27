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
                exclude-result-prefixes="d"
                version="1.0">

  <xsl:import href="urn:docbkx:stylesheet"/>
  <xsl:import href="common.xsl"/>

  <!-- Need a doctype -->
  <xsl:output method="html"
              encoding="ISO-8859-1"
              indent="no"
              doctype-system=""/>

  <!-- Stylesheet -->
  <xsl:param name="html.stylesheet">css/bootstrap/html.css</xsl:param>

  <!-- Javascript -->
  <xsl:template name="user.head.content">
    <script src="js/bootstrap/jquery-1.7.1.min.js"></script>
    <script src="js/bootstrap/bootstrap-2.0.2.min.js"></script>
    <script src="js/bootstrap/docbook.js"></script>
    <script src="js/bootstrap/google-code-prettify/prettify.js"></script>
  </xsl:template>

  <xsl:template name="user.header.content">
    <div class='navbar navbar-fixed-top'>
      <div class='navbar-inner'>
        <div class='container'>

          <a id='topbar' class='brand'></a>

          <div class='nav-collapse'>

            <!-- Primary nav -->
            <ul id='primarynav' class='nav'></ul>

            <!-- Secondary nav -->
            <ul class='nav pull-right'>
              <li class='dropdown'>
                <a href='#' class='dropdown-toggle' data-toggle='dropdown'>Powered by Wikbook</a>
                <ul class='dropdown-menu'>
                  <li><a href='http://www.github.com/vietj/wikbook'>Project</a></li>
                  <li><a href='http://vietj.github.com/wikbook/'>Documentation</a></li>
                  <li><a href='http://jira.exoplatform.org/browse/WKBK'>Issue tracker</a></li>
                </ul>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </xsl:template>

</xsl:stylesheet>