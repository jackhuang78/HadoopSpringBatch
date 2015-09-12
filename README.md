# HadoopSpringBatch
A workflow engine for Hadoop, powered by Spring Batch.

## Features
- Workflow with Pig/Hive scripts or generic shell commands.
- Parameter management.
- Sequential or parallel script executions.
- Easy setup for Java/Python UDFs. 

## Requirement
To build the project, you will need the following installed:
- Java
- Gradle

To run the example workflow, you will need the following installed on your Hadoop cluster
- Pig
- Hive
- HCatalog
- Python



## Installation
1. [Download](https://github.com/jhuang78/HadoopSpringBatch/archive/master.zip) and unzip the repository.
2. Edit/create `gradle.properties` to set cluster connection information:

		hsb_host=<host>
		hsb_username=<username>
		hsb_password=<password>

3. Execute `gradle deploy` to both build and deploy the code to the cluster.
4. SSH into the cluster and CD into the unzipped project directory.
5. Run the setup workflow with `script/launch.sh jobs.xml setup`
6. Run the process workflow with `script/launch.sh jobs.xml job1`
7. Look at `src/resources/jobs.xml` to see the workflow setup.

## Contact
Jack Huang (jack.huang78@gmail.com)