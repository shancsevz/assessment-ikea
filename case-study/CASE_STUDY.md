# Case Study Scenarios to discuss

## Scenario 1: Cost Allocation and Tracking
**Situation**: The company needs to track and allocate costs accurately across different Warehouses and Stores. The costs include labor, inventory, transportation, and overhead expenses.

**Task**: Discuss the challenges in accurately tracking and allocating costs in a fulfillment environment. Think about what are important considerations for this, what are previous experiences that you have you could related to this problem and elaborate some questions and considerations

**Response:**

**Action**: Firtst we have to build a agnostic cost-tracking system with a reliable and quick synchronization mechanism across all the systems like inventories, labour cost and other
key areas which will have impact. Another key is we need a concrete Order Manager system which should be a Gatekeeper for all these system to make a final financial decision on payment.
This system will usually talks to Geo Labour costs, SEPA/SWIFT, and how well all these are integrated to give a right cost on every situation changes.
I have worked in a similar system for a large bank, where we offloaded a lot of unwanted calls to give real-time info using events and also architected well with taking care of key things like 
Idempotent, how well APIs are designed, handling of rollbacks, Real-time dashboards are some of the key things to consider. 

**Result**: Once we build the right architecture with all kind of fallback and rollbacks, we can have a real-time cost tracking 
system which will give us the right info to make the right decision on time and also we can have a better cost control 
and optimization strategy in place. 
----
## Scenario 2: Cost Optimization Strategies
**Situation**: The company wants to identify and implement cost optimization strategies for its fulfillment operations. The goal is to reduce overall costs without compromising service quality.

**Task**: Discuss potential cost optimization strategies for fulfillment operations and expected outcomes from that. How would you identify, prioritize and implement these strategies?

**Response:**

**Action**: The right automation tools can serve as the first step toward optimizing costs in fulfillment operations while also enabling better control mechanisms to prevent inefficiencies or operational spillovers. 
It is important to identify repetitive processes and smaller tasks where time and resources are being unnecessarily spent, and then address those areas with appropriate automation solutions.
In the past, when I worked on a similar challenge at a large telecom company, we implemented a machine learning–based **AutoLoader** tool. 
This system helped predict future demand and supported employees in proactively moving items to the most appropriate warehouses based on demand patterns in specific geographic locations.
As a result, the organization was able to improve inventory positioning, reduce unnecessary movements, and increase overall operational efficiency.

**Result**: The ROI of this tool was significant. It improved accuracy to nearly 94%, which resulted in substantial cost savings for the company at that time.

----
## Scenario 3: Integration with Financial Systems
**Situation**: The Cost Control Tool needs to integrate with existing financial systems to ensure accurate and timely cost data. The integration should support real-time data synchronization and reporting.

**Task**: Discuss the importance of integrating the Cost Control Tool with financial systems. What benefits the company would have from that and how would you ensure seamless integration and data synchronization?

**Response:**

**Action**:Integrating a Cost Control Tool with the company’s financial systems is essential for ensuring accurate, consistent, and real-time visibility of operational costs.
In a fulfillment environment where expenses such as labor, inventory handling, transportation, and overhead are generated across multiple warehouses and stores, 
financial integration helps ensure that operational data is correctly reflected in financial reporting and decision-making. Sometime back we have developed extensive APIs to give 
a high-level of details to the financial systems to track the purpose of the cost in a better way.

**Result**: This intgerations help to find the right cost centers and the drivers for it.

----
## Scenario 4: Budgeting and Forecasting
**Situation**: The company needs to develop budgeting and forecasting capabilities for its fulfillment operations. The goal is to predict future costs and allocate resources effectively.

**Task**: Discuss the importance of budgeting and forecasting in fulfillment operations and what would you take into account designing a system to support accurate budgeting and forecasting?

**Response:** 

**Action**: It's a very good and common use case for Machine learning, Understanding the data better and make sure we have enormous data and find how we can improvise the data will help us to make the prediction in a better way.
Forecast will help to create a better strategy for budgeting and also gives a better control of the cost. Data cleanup and understand of data will help us to improve the accuracy.
Similar use-cases we have done in past using ML based solution. where we have a build a model from various sources including how the Employee travels and what will be the weather and so on. 

**Result**: We can able to predict the future with Demand vs Forecast and also the surplus and deficit in the inventory. which help us to plan the future in a better way.

-----

## Scenario 5: Cost Control in Warehouse Replacement
**Situation**: The company is planning to replace an existing Warehouse with a new one. The new Warehouse will reuse the Business Unit Code of the old Warehouse. The old Warehouse will be archived, but its cost history must be preserved.

**Task**: Discuss the cost control aspects of replacing a Warehouse. Why is it important to preserve cost history and how this relates to keeping the new Warehouse operation within budget?

**Response:**

**Action**: The Auditability of teh past cost history is very important as this will be the key info to build the new Warehouse in a better way and understand its cost drivers. As we discussed in the previous 
Tasks the about Budgeting and Forecasting this will be the key info to derive those. We can visuallize a long-term trend on how the warehouse behaved in the past and what are the 
insights we can be able derive out of it.

**Result**: We can able to stand in the current financial continuity and workforce continuity. By leveraging historical data, the company can better monitor operational costs and ensure that the new warehouse operates within budget while improving overall fulfillment performance

-----

## Instructions for Candidates
Before starting the case study, read the [BRIEFING.md](BRIEFING.md) to quickly understand the domain, entities, business rules, and other relevant details.

**Analyze the Scenarios**: Carefully analyze each scenario and consider the tasks provided. To make informed decisions about the project's scope and ensure valuable outcomes, what key information would you seek to gather before defining the boundaries of the work? Your goal is to bridge technical aspects with business value, bringing a high level discussion; no need to deep dive.
