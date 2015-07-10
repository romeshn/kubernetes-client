package io.fabric8.kubernetes;

import io.fabric8.common.Builder;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateExamples {

    private static final Logger logger = LoggerFactory.getLogger(UpdateExamples.class);

    public static void main(String[] args) {
        KubernetesClient client = null;

        try {
            client = new KubernetesClient("https://localhost:8443/api/v1/");

            System.out.println(
                    client.pods("rabbitmq-pod").inNamespace("default").update(
                            new Resource.ResourceUpdate<Pod>() {
                                @Override
                                public Pod update(Pod pod) {
                                    pod.getMetadata().getLabels().put("this", "works");
                                    return pod;
                                }
                            }
                    )
            );

            System.out.println(
                    client.pods("rabbitmq-pod").inNamespace("default").update(
                            new Resource.BuilderUpdate<Pod, Builder<Pod>>() {
                                @Override
                                public Pod update(Builder<Pod> builder) {
                                    PodBuilder podBuilder = (PodBuilder) builder;;
                                    return podBuilder.editMetadata().addToLabels("i", "rock").endMetadata().build();
                                }
                            }
                    )
            );
        } catch (KubernetesClientException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (client != null) {
                client.close();
            }
        }
    }

}