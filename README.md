# HadoopSpringBatch
A SpringBatch setup for developing workflow on Hadoop clusters. 

## Motivation
When processing data on Hadoop cluster, it is often more managable to break down a big process into smaller steps. This is even more the case in a team setup, where the flow of data from one step to the next must be clearly defined. This project aims to provide an intuitive way of developers to define a workflow on Hadoop cluster. In particular:

- You can create many Pig, Hive, or Shell scripts as different steps, which then can be ordered and executed in sequence or parallel. 
- You can centrally define all parameters required by the scripts (e.g. path to input or output) and refer to them in the scripts.
- You can do all these with zero knowledge on Spring Batch (which is the workflow framework I am using to implement this project). 


## Usage
Download the repository. In `gradle.build`, find the part that specify the hadoop cluster 

    remotes {
      mapr {
        role 'cluster'
        host = '192.168.192.128'
        user = 'mapr'
        password = 'mapr'
      }
    }
    
and change the host IP, username, and password to your cluster. If you don't have one handy, [MapR's sandbox](https://www.mapr.com/products/mapr-sandbox-hadoop/download) is a good substitute.

The run `gradle deploy`. This will build the project, zip the code, scp it to the cluster, and unzip it there.

Finally, navigate to the project's root directory and run the example workflow with `script/launcher.sh jobs.xml job1`.

More like than not you will hit some problem setting up this framework, so just show me an email if you are really interested in using this tool and I'll help you set it up.

