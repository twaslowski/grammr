resource "vercel_project_environment_variable" "backend_production" {
  project_id = vercel_project.project.id
  key        = "BACKEND_HOST"
  value      = var.backend_host_production
  target     = ["production"]
  comment    = "API backend for production"
}

resource "vercel_project_environment_variable" "clerk_publishable_key_production" {
  project_id = vercel_project.project.id
  key        = "NEXT_PUBLIC_CLERK_PUBLISHABLE_KEY"
  value      = var.clerk_publishable_key_production
  target     = ["production"]
  comment    = "Clerk publishable key for production"
}

resource "vercel_project_environment_variable" "clerk_secret_key_production" {
  project_id = vercel_project.project.id
  key        = "CLERK_SECRET_KEY"
  value      = var.clerk_secret_key_production
  target     = ["production"]
  comment    = "Clerk secret key for production"
}

resource "vercel_project_environment_variable" "tts_host_production" {
  project_id = vercel_project.project.id
  key        = "TTS_HOST"
  value      = "https://b4l7c733qf.execute-api.eu-central-1.amazonaws.com/dev/tts"
  target     = ["production"]
  comment    = "TTS API Gateway URL"
}

resource "vercel_project_environment_variable" "backend_preview" {
  project_id = vercel_project.project.id
  key        = "BACKEND_HOST"
  value      = var.backend_host_preview
  target     = ["preview"]
  git_branch = var.tracked_staging_branch
  comment    = "API backend for staging"
}

resource "vercel_project_environment_variable" "clerk_publishable_key_preview" {
  project_id = vercel_project.project.id
  key        = "NEXT_PUBLIC_CLERK_PUBLISHABLE_KEY"
  value      = var.clerk_publishable_key_preview
  target     = ["preview"]
  git_branch = var.tracked_staging_branch
  comment    = "Clerk publishable key for preview"
}

resource "vercel_project_environment_variable" "clerk_secret_key_preview" {
  project_id = vercel_project.project.id
  key        = "CLERK_SECRET_KEY"
  value      = var.clerk_secret_key_preview
  target     = ["preview"]
  git_branch = var.tracked_staging_branch
  comment    = "Clerk secret key for preview"
}

resource "vercel_project_environment_variable" "tts_host_preview" {
  project_id = vercel_project.project.id
  key        = "TTS_HOST"
  value      = "https://b4l7c733qf.execute-api.eu-central-1.amazonaws.com/dev/tts"
  target     = ["preview"]
  git_branch = var.tracked_staging_branch
  comment    = "TTS API Gateway URL"
}