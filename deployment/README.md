# deployment

All scripts running performance tests and metrics tests based on Ansible playbooks and python

source /Users/grzegorz.miejski/home/workspaces/private/magisterka/movies-recommender-api/deployment/scripts/runner/bin/activate

## APPLICATION METRICS

### Prepare datasets for local run

use scripts/kcrossvalidation/FoldsCreator.py 

### Running metrics locally:
`./run-metrics-local.sh [dataset]` 

example:
`./run-metrics-local.sh ml-100k`

Or to run all metrics for given settings run:

`python local_accuraty_metrics_runner.py`


## Running performance tests:

### local run:


