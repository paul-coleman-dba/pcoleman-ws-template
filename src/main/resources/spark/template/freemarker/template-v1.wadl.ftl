<application xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xmlns:xsd="http://www.w3.org/2001/XMLSchema"
             xmlns:xs="http://www.w3.org/2001/XMLSchema"
             xmlns="http://wadl.dev.java.net/2009/02"
             xmlns:pcoleman="http://pcoleman-ws-template.herokuapp.com/schema"
             xsi:schemaLocation="http://wadl.dev.java.net/2009/02">
    <doc>
        <![CDATA[Feed Item Template]]>
    </doc>
    <grammars>
        <xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema" targetNamespace="http://pcoleman-ws-template.herokuapp.com/schema" xmlns:pcoleman="http://pcoleman-ws-template.herokuapp.com/schema">
            <xs:element name="FeedRequest" type="pcoleman:FeedRequest"/>
            <xs:complexType name="FeedRequest">
                <xs:sequence>
                    <xs:element name="transactionId" type="xs:string" nillable="true" minOccurs="0" maxOccurs="1"/>
                    <xs:element name="dob" type="xs:string" nillable="true" minOccurs="0" maxOccurs="1"/>
                    <xs:element name="gender" type="xs:string" nillable="true" minOccurs="0" maxOccurs="1"/>
                </xs:sequence>
            </xs:complexType>
            <xs:element name="FeedResponse" type="pcoleman:FeedResponse"/>
            <xs:complexType name="FeedResponse">
                <xs:sequence>
                    <xs:element name="messageId" type="xs:string" nillable="true" minOccurs="0" maxOccurs="1"/>
                    <xs:element name="statusCode" type="xs:string" nillable="true" minOccurs="0" maxOccurs="1"/>
                    <xs:element name="statusMessage" type="xs:string" nillable="true" minOccurs="0" maxOccurs="1"/>
                    <xs:element name="transactionId" type="xs:string" nillable="true" minOccurs="0" maxOccurs="1"/>
                </xs:sequence>
            </xs:complexType>
        </xs:schema>
    </grammars>

    <resources base="https://pcoleman-ws-template.herokuapp.com">
        <resource path="/feed/v1/script">
            <doc>
                <![CDATA[Receives a Feed Item]]>
            </doc>
            <method name="POST" id="script">
                <request>
                    <doc>
                        <![CDATA[Bearer token]]>
                    </doc>
                    <param name="Authorization" repeating="false" required="true" type="xs:string" style="header" xmlns:xs="http://www.w3.org/2001/XMLSchema"/>
                    <representation mediaType="application/json" element="pcoleman:FeedRequest"/>
                </request>
                <response status="200">
                    <doc>
                        <![CDATA[Feed item received and queued successfully]]>
                    </doc>
                    <representation mediaType="application/json" element="pcoleman:FeedResponse"/>
                </response>
                <response status="403">
                    <doc>
                        <![CDATA[Invalid Authorization token]]>
                    </doc>
                    <representation mediaType="application/json" element="pcoleman:FeedResponse"/>
                </response>
                <response status="401">
                    <doc>
                        <![CDATA[Invalid transport protocol - HTTPs required]]>
                    </doc>
                    <representation mediaType="application/json" element="pcoleman:FeedResponse"/>
                </response>
            </method>
        </resource>
    </resources>

</application>