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

  <xsl:template name="user.head.content">
    <meta name="viewport" content="width=device-width, initial-scale=1.0"></meta>
    <script src="js/bootstrap/jquery-1.7.1.min.js"></script>
    <script src="js/bootstrap/bootstrap-2.2.2.min.js"></script>
    <script src="js/bootstrap/docbook-sidebar.js"></script>
    <script src="js/bootstrap/google-code-prettify/prettify.js"></script>
  </xsl:template>

</xsl:stylesheet>