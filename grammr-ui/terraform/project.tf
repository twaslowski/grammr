resource "vercel_project" "project" {
  name          = "grammr"
  framework     = "nextjs"
  public_source = false

  root_directory = "grammr-ui"

  git_repository = {
    type = "github"
    repo = "twaslowski/grammr"
  }
}

resource "vercel_project_domain" "domain_production" {
  project_id           = vercel_project.project.id
  domain               = "grammr.app"
  redirect_status_code = 307
}

resource "vercel_project_domain" "domain_production_www" {
  project_id = vercel_project.project.id
  domain     = "www.grammr.app"
}

resource "vercel_project_domain" "domain_preview" {
  project_id = vercel_project.project.id
  domain     = "dev.grammr.app"
  git_branch = "develop"
}