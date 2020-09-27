package be.hehehe.lapinstance.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import be.hehehe.lapinstance.model.RaidSubscription;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebSocketService {

	private final SimpMessagingTemplate simpMessagingTemplate;

	public void notifyRaidSubscriptionChanged(RaidSubscription subscription) {
		String topic = String.format("/topic/raid/%s/subscriptions", subscription.getRaid().getId());
		simpMessagingTemplate.convertAndSend(topic, subscription);
	}

}
