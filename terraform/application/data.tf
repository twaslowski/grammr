data "external" "git_hash" {
  program = ["git", "log", "--pretty=format:{ \"sha\": \"%H\" }", "-1", "HEAD"]
}
