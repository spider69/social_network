package com.yusupov.social_network.database.handlers

import akka.actor.Actor.Receive
import akka.actor.ActorRef

trait Handler {
  def handle(sender: ActorRef): Receive
}
