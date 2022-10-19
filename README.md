## Demonstrates using Google Cloud Deploy with Cloud Run

![](artifacts/Cloud%20Run.png)

This is a complete pipeline demonstrating:
1. Continuous integration process once a code is merged into the "main" branch of a project
2. Building an image once the tests are done
3. Deploying the image into Cloud Run Dev App
4. Manually promoting it into Cloud Run Prod App

## Resources

- cloudbuild.yaml holds the CI pipeline steps
- clouddeploy.yaml holds the CD pipeline steps

## Deployment

- Create a Cloud Build Trigger:
  - Using [Cloud Console](https://cloud.google.com/build/docs/automating-builds/create-manage-triggers)
  - OR using command line:
    ```shell
    gcloud beta builds triggers create github --repo-name clouddeploy-cloudrun-sample --repo-owner bijukunjummen --branch-pattern '^main$' --name clouddeploy-cloudrun-sample --build-config cloudbuild.yaml
    ```
- Create a Deployment Pipeline -
    ```shell
    gcloud deploy apply --file=clouddeploy.yaml --region=us-west1
    ```
