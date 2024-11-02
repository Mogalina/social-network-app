package org.example.service;

import org.example.models.User;

import java.util.*;

/**
 * Community class responsible for analyzing user relationships within a network.
 */
public class Community {

    // The social network the community is based on
    private final Network network;

    // Mapping of user identifiers and their friends
    private Map<String, List<String>> relations;

    /**
     * Constructs a new Community with the specified {@link Network}.
     *
     * @param network the network that the community is based on
     */
    public Community(Network network) {
        this.network = network;
        this.relations = Collections.unmodifiableMap(network.getRelations());
    }

    /**
     * Performs a Depth-First Search (DFS) traversal to discover all friends connected to a user.
     *
     * @param uid the user identifier to start the traversal from
     * @param seen a set to keep track of seen users to avoid cycles
     * @param communityUsers a list to store users that are part of the same community
     */
    private void dfsTraversal(String uid, Set<String> seen, List<String> communityUsers) {
        for (String friendId : relations.get(uid)) {
            if (!seen.contains(friendId) && network.isFriendship(uid, friendId)) {
                seen.add(friendId);
                communityUsers.add(friendId);
                dfsTraversal(friendId, seen, communityUsers);
            }
        }
    }

    /**
     * Counts the number of distinct communities in the network.
     *
     * @return the total number of communities found
     */
    public int countCommunities() {
        relations = Collections.unmodifiableMap(network.getRelations());
        int communities = 0;
        Set<String> seen = new HashSet<>();
        List<String> communityUsers = new ArrayList<>();

        for (String uid : relations.keySet()) {
            if (!seen.contains(uid)) {
                seen.add(uid);
                communityUsers.add(uid);
                dfsTraversal(uid, seen, communityUsers);
                communities++;
            }
        }

        return communities;
    }

    /**
     * Finds the most social community, which is the community with the largest number of users.
     *
     * @return an iterable collection of {@link User} objects in the largest community
     */
    public Iterable<User> getMostSocialCommunity() {
        relations = Collections.unmodifiableMap(network.getRelations());
        Set<String> seen = new HashSet<>();
        List<String> currentCommunityUsers = new ArrayList<>();
        List<String> largestCommunityUsers = new ArrayList<>();

        for (String uid : relations.keySet()) {
            if (!seen.contains(uid)) {
                seen.add(uid);
                currentCommunityUsers.add(uid);
                dfsTraversal(uid, seen, currentCommunityUsers);

                if (currentCommunityUsers.size() > largestCommunityUsers.size()) {
                    largestCommunityUsers = currentCommunityUsers;
                }
                currentCommunityUsers = new ArrayList<>();
            }
        }

        return largestCommunityUsers.stream()
                .map(network::findUser)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }
}
