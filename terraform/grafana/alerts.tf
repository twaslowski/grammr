resource "grafana_rule_group" "grammr_rules" {
  name             = "grammr-alerting"
  folder_uid       = grafana_folder.base_folder.uid
  interval_seconds = 300

  rule {
    name      = "Morphology Duration"
    condition = "C"

    data {
      ref_id = "A"

      relative_time_range {
        from = 21600
        to   = 0
      }

      datasource_uid = data.grafana_data_source.cloudwatch.id
      model          = "{\"alias\":\"\",\"datasource\":{\"type\":\"cloudwatch\",\"uid\":\"${data.grafana_data_source.cloudwatch.id}\"},\"dimensions\":{\"FunctionName\":[\"morphology-ru-prod\"]},\"expression\":\"SEARCH('{AWS/Lambda,FunctionName} FunctionName=~\\\"*-prod\\\" \\\"Invocations\\\"', \\\"Average\\\", 300)\",\"id\":\"\",\"instant\":false,\"intervalMs\":1000,\"label\":\"\",\"logGroups\":[],\"matchExact\":true,\"maxDataPoints\":43200,\"metricEditorMode\":1,\"metricName\":\"Duration\",\"metricQueryType\":0,\"namespace\":\"AWS/Lambda\",\"period\":\"\",\"queryLanguage\":\"CWLI\",\"queryMode\":\"Metrics\",\"range\":true,\"refId\":\"A\",\"region\":\"eu-central-1\",\"sqlExpression\":\"\",\"statistic\":\"Average\"}"
    }
    data {
      ref_id = "B"

      relative_time_range {
        from = 0
        to   = 0
      }

      datasource_uid = "__expr__"
      model          = "{\"conditions\":[{\"evaluator\":{\"params\":[],\"type\":\"gt\"},\"operator\":{\"type\":\"and\"},\"query\":{\"params\":[\"B\"]},\"reducer\":{\"params\":[],\"type\":\"last\"},\"type\":\"query\"}],\"datasource\":{\"type\":\"__expr__\",\"uid\":\"__expr__\"},\"expression\":\"A\",\"intervalMs\":1000,\"maxDataPoints\":43200,\"reducer\":\"last\",\"refId\":\"B\",\"type\":\"reduce\"}"
    }
    data {
      ref_id = "C"

      relative_time_range {
        from = 0
        to   = 0
      }

      datasource_uid = "__expr__"
      model          = "{\"conditions\":[{\"evaluator\":{\"params\":[1],\"type\":\"lt\"},\"operator\":{\"type\":\"and\"},\"query\":{\"params\":[\"C\"]},\"reducer\":{\"params\":[],\"type\":\"last\"},\"type\":\"query\"}],\"datasource\":{\"type\":\"__expr__\",\"uid\":\"__expr__\"},\"expression\":\"B\",\"intervalMs\":1000,\"maxDataPoints\":43200,\"refId\":\"C\",\"type\":\"threshold\"}"
    }

    no_data_state  = "NoData"
    exec_err_state = "Error"
    for            = "5m"
    annotations = {
      __dashboardUid__ = grafana_dashboard.lambdas.uid
      __panelId__      = "2"
      runbook_url      = "https://246770851643-spg54edx.eu-central-1.console.aws.amazon.com/lambda/home?region=eu-central-1#/functions"
      summary          = "Morphology Lambdas are down"
    }
    is_paused = false

    notification_settings {
      contact_point = local.grafana_contact_point
      group_by      = null
      mute_timings  = null
    }
  }
}
