{
  "swagger" : "2.0",
  "info" : {
    "version" : "1.0",
    "title" : "API",
    "description" : "P Coleman Web Service Template"
  },
  "host" : "pcoleman-ws-template.herokuapp.com",
  "basePath" : "/",
  "schemes" : [
    "https"
  ],
  "consumes" : [
    "application/json"
  ],
  "produces" : [
    "application/json"
  ],
  "securityDefinitions": {
    "Bearer": {
      "type": "apiKey",
      "name": "Authorization",
      "in": "header"
    }
  },
  "paths" : {
    "/feed/v1/script" : {
      "post" : {
        "description" : "Receives a Feed Item",
        "summary" : "script",
        "operationId" : "FeedV1ScriptPost",
        "produces" : [
          "application/json"
        ],
        "parameters" : [
          {
            "name" : "Authorization",
            "in" : "header",
            "required" : true,
            "type" : "string",
            "description" : "Bearer token"
          },
          {
            "name" : "FeedRequest",
            "in" : "body",
            "required" : true,
            "description" : "",
            "schema" : {
              "$ref" : "#/definitions/FeedRequest"
            }
          }
        ],
        "responses" : {
          "200" : {
            "description" : "Feed Item received and queued successfully",
            "schema" : {
              "$ref" : "#/definitions/FeedResponse"
            },
            "examples" : {}
          },
          "403" : {
            "description" : "Invalid Authorization token",
            "schema" : {
              "$ref" : "#/definitions/FeedResponse"
            },
            "examples" : {}
          },
          "401" : {
            "description" : "Invalid transport protocol - HTTPs required",
            "schema" : {
              "$ref" : "#/definitions/FeedResponse"
            },
            "examples" : {}
          }
        },
        "security": [
          {
            "Bearer": []
          }
        ],
        "x-unitTests" : [],
        "x-operation-settings" : {
          "CollectParameters" : false,
          "AllowDynamicQueryParameters" : false,
          "AllowDynamicFormParameters" : false,
          "IsMultiContentStreaming" : false
        }
      }
    }
  },
  "definitions" : {
    "FeedRequest" : {
      "title" : "FeedRequest",
      "description" : "",
      "type" : "object",
      "properties" : {
        "transactionId" : {
          "description" : "",
          "type" : "string"
        },
        "dob" : {
          "description" : "",
          "type" : "string"
        },
        "gender" : {
          "description" : "",
          "type" : "string"
        }
      }
    },
    "FeedResponse" : {
      "title" : "FeedResponse",
      "description" : "",
      "type" : "object",
      "properties" : {
        "messageId" : {
          "description" : "",
          "type" : "string"
        },
        "statusCode" : {
          "description" : "",
          "type" : "string"
        },
        "statusMessage" : {
          "description" : "",
          "type" : "string"
        },
        "transactionId" : {
          "description" : "",
          "type" : "string"
        }
      }
    }
  }
}