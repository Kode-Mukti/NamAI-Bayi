# 22 — Analytics Events

## Summary

Complete analytics tracking plan for NamAI Bayi. Every user action is tracked with consistent naming, properties, and context. Events power product decisions, AI quality improvement, and business reporting.

## Reasoning

Without analytics, product decisions are guesses. Every event is designed to answer a specific question about user behavior, AI quality, or business performance. Events are implemented from day one.

## Recommendation

### Analytics Platform: Amplitude (primary) + Firebase (secondary)

### Event Naming Convention

```
[Object]_[Action]         # snake_case
Examples:
  page_viewed
  name_saved
  consultation_completed
  share_initiated
```

### User Properties

| Property | Type | Description | Set At |
|----------|------|-------------|--------|
| user_id | String | Anonymous ID (device-based) | First launch |
| platform | String | android / web | First launch |
| app_version | String | Semver | First launch |
| religion | String | From input | First consultation |
| culture | String | From input | First consultation |
| province | String | From input | First consultation |
| total_consultations | Integer | Count | Incremented |
| total_names_saved | Integer | Count | Incremented |
| total_shares | Integer | Count | Incremented |
| is_premium | Boolean | Premium status | On change |
| preferred_strategy | String | Most used strategy | After 5 consultations |

### Core Events

#### Onboarding & Session

| Event | Trigger | Properties |
|-------|---------|------------|
| app_launched | App open / page load | source, is_returning |
| home_viewed | Home screen rendered | session_count |
| consultation_started | Tap "Mulai Konsultasi" | source |
| input_field_filled | Any input field completed | field_name, field_value |
| input_form_submitted | Submit "Cari Nama" | field_count, has_advanced_fields |
| input_form_abandoned | Leave without submit | fields_filled_count, time_spent |

#### AI Consultation

| Event | Trigger | Properties |
|-------|---------|------------|
| ai_generation_started | API call initiated | strategy_selected |
| ai_generation_completed | Response received | strategy, response_time_ms, name_count |
| ai_generation_failed | Error response | error_code, error_message, strategy |
| ai_generation_retried | User retried | previous_error |
| result_viewed | Result screen rendered | strategy, recommendation_count |
| name_detail_viewed | Tap name card | name, strategy, uniqueness_score |
| strategy_badge_tapped | Tap strategy info | strategy |

#### Engagement

| Event | Trigger | Properties |
|-------|---------|------------|
| name_saved | Tap heart icon | name, strategy, uniqueness_score |
| name_unsaved | Tap heart (remove) | name |
| name_marked_chosen | Tap "Pilih" | name, strategy |
| name_chosen_removed | Remove chosen status | name |
| wishlist_viewed | Open wishlist | saved_count, chosen_count |
| consultation_restarted | Tap "Konsultasi Lagi" | previous_strategy, changed_fields |

#### Sharing

| Event | Trigger | Properties |
|-------|---------|------------|
| share_initiated | Tap share button | share_target (wa/ig/link), name |
| share_completed | Share success | share_target, name |
| share_cancelled | Share dismissed | share_target, name |
| public_link_created | Generate link | name_count |
| public_link_viewed | External visit | referrer, name_count |
| battle_started | Start name battle | name_a, name_b |
| battle_voted | Vote in battle | winner_name, loser_name |
| battle_shared | Share battle result | winner_name |

#### Daily & Retention

| Event | Trigger | Properties |
|-------|---------|------------|
| daily_name_viewed | Open daily name push | name, strategy |
| daily_name_saved | Save daily name | name |
| push_notification_opened | Open from push | notification_type, name |
| push_notification_received | Push delivered | notification_type |

#### Premium (Future)

| Event | Trigger | Properties |
|-------|---------|------------|
| premium_offer_viewed | Premium screen shown | source, price |
| premium_subscription_started | Begin payment | plan_type, price |
| premium_subscription_completed | Payment success | plan_type, price, payment_method |
| premium_subscription_failed | Payment failed | plan_type, error_code |
| premium_subscription_cancelled | Cancel subscription | plan_type, duration_days |

#### Quality & Feedback

| Event | Trigger | Properties |
|-------|---------|------------|
| name_feedback_given | Rate name (thumbs) | name, rating (up/down), strategy |
| app_rated | Rate on Play Store | rating |
| feedback_submitted | Send feedback | message, category |
| error_displayed | Error shown to user | error_code, screen, context |

### Event Property Taxonomy

| Property | Type | Values |
|----------|------|--------|
| source | String | home_button, daily_push, external_link, deep_link |
| platform | String | android, web |
| strategy | String | Islami, Modern, Jawa, Sunda, etc. |
| share_target | String | whatsapp, instagram, link, qr_code |
| error_code | String | AI_TIMEOUT, AI_INVALID, NETWORK_ERROR, RATE_LIMITED |
| screen | String | home, input, result, wishlist, loading |
| religion | String | Islam, Kristen, Katolik, Hindu, Buddha, Konghucu |
| culture | String | Jawa, Sunda, Bali, Batak, etc. |
| gender | String | laki_laki, perempuan |

### Conversion Funnels

#### Funnel 1: Consultation Flow

```
app_launched → consultation_started → input_form_submitted → ai_generation_completed → result_viewed
    100%            45%                     85%                     92%                     95%
```

#### Funnel 2: Engagement Flow

```
result_viewed → name_saved → wishlist_viewed → name_marked_chosen
     100%          30%            60%               25%
```

#### Funnel 3: Viral Flow

```
result_viewed → share_initiated → share_completed → (external) → app_launched (new user)
     100%           15%              80%                 5%
```

### Dashboard Queries

| Query | Purpose | Events Used |
|-------|---------|-------------|
| Daily Active Users | Track growth | app_launched |
| Consultation Volume | Track usage | ai_generation_completed |
| Share Rate | Track virality | share_completed / result_viewed |
| Save Rate | Track engagement | name_saved / result_viewed |
| AI Success Rate | Track quality | ai_generation_completed / ai_generation_started |
| Strategy Distribution | Track AI behavior | ai_generation_completed.strategy |
| Top Saved Names | Track popular names | name_saved.name |
| Premium Conversion | Track revenue | premium_subscription_completed |
| Funnel Drop-off | Find issues | All funnel events |
| User Retention | Track stickiness | app_launched (cohort) |

### Privacy & Consent

- No PII tracked (names are event properties but not linked to identity)
- Device-based anonymous ID (not requiring login)
- Clear privacy policy explaining analytics
- Opt-out option in settings (future)
- GDPR and UU PDP compliance

## Implementation

- Implement Amplitude SDK in both platforms
- Create analytics utility/service class
- Track all events in development before launch
- Set up dashboards before launch
- Configure alerts for metric anomalies

## Future Improvement

- Real-time analytics dashboard
- Machine learning on user behavior
- Predictive churn model
- Personalized recommendations based on analytics
- Automated A/B test analysis
